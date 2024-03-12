package jjabtwitter.follow.application;

import jjabtwitter.follow.domain.Follow;
import jjabtwitter.global.exception.ClientException;
import jjabtwitter.member.application.MemberService;
import jjabtwitter.member.application.dto.MemberId;
import jjabtwitter.support.IntegrationTest;
import jjabtwitter.support.MemberTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static jjabtwitter.global.exception.ExceptionInformation.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@IntegrationTest
class FollowServiceTest {

    @Autowired
    private FollowService followService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberTestSupport memberTestSupport;

    private MemberId 팔로워_id;
    private Long 팔로잉_id;

    @BeforeEach
    void 회원_두명을_가입_시킨다() {
        팔로워_id = new MemberId(memberTestSupport.create().build().getId());
        팔로잉_id = memberTestSupport.create().build().getId();
    }

    @Nested
    @DisplayName("팔로우 테스트")
    class FollowTest {
        @Test
        void 팔로우가_정상적으로_저장되는지_알아본다() {
            final Follow follow = followService.followMember(팔로워_id, 팔로잉_id);

            assertThat(follow).isNotNull();
        }

        @Test
        void 회원들은_서로_팔로우_할_수_있다() {
            final Follow AfollowB = followService.followMember(팔로워_id, 팔로잉_id);
            final Follow BfollowA = followService.followMember(new MemberId(팔로잉_id), 팔로워_id.id());

            assertThat(AfollowB).isNotEqualTo(BfollowA);
        }

        @Test
        void 팔로잉이_존재하지_않는_회원이라면_예외가_발생한다() {
            assertThatThrownBy(() -> followService.followMember(팔로워_id, -1L))
                    .isExactlyInstanceOf(ClientException.class)
                    .hasMessage(MEMBER_NOT_FOUND.getMessage());

        }

        @Test
        void 팔로잉이_삭제된_회원이면_예외가_발생한다() {
            // given
            final MemberId followingMemberId = new MemberId(팔로잉_id);

            // when
            memberService.withdraw(followingMemberId);

            // then
            assertThatThrownBy(() -> followService.followMember(팔로워_id, 팔로잉_id))
                    .isExactlyInstanceOf(ClientException.class)
                    .hasMessage(MEMBER_NOT_FOUND.getMessage());
        }

        @Test
        void 이미_팔로우한_계정을_다시_팔로우_할_수_없다() {
            followService.followMember(팔로워_id, 팔로잉_id);

            assertThatThrownBy(() -> followService.followMember(팔로워_id, 팔로잉_id))
                    .isExactlyInstanceOf(ClientException.class)
                    .hasMessage(FOLLOW_ALREADY_EXIST.getMessage());
        }

        @Test
        void 자기_자신을_팔로우_할_수_없다() {
            assertThatThrownBy(() -> followService.followMember(팔로워_id, 팔로워_id.id()))
                    .isExactlyInstanceOf(ClientException.class)
                    .hasMessage(FOLLOW_SELF_INVALID.getMessage());
        }

        @Test
        void 같은_팔로우_요청이_동시에_들어오면_실패한다() throws InterruptedException {
            // given
            int numberOfExecute = 5;
            AtomicInteger successCount = new AtomicInteger();
            AtomicInteger failCount = new AtomicInteger();
            ExecutorService service = Executors.newFixedThreadPool(5);
            CountDownLatch latch = new CountDownLatch(numberOfExecute);

            // when
            for (int i = 0; i < numberOfExecute; i++) {
                service.execute(() -> {
                    try {
                        followService.followMember(팔로워_id, 팔로잉_id);
                        successCount.getAndIncrement();
                        System.out.println("성공");
                    } catch (ClientException e) {
                        failCount.getAndIncrement();
                        System.out.println("충돌감지" + e.getClass() + e.getMessage());
                    } catch (Exception e) {
                        failCount.getAndIncrement();
                        System.out.println("정의 안된 예회원" + e.getClass());
                        System.out.println(e.getMessage());
                    }
                    latch.countDown();
                });
            }
            latch.await();

            // then
            assertSoftly(softly -> {
                        softly.assertThat(successCount.get()).isEqualTo(1);
                        softly.assertThat(failCount.get()).isEqualTo(4);
                    }
            );
        }
    }

    @Nested
    @DisplayName("언팔로우 테스트")
    class UnfollowTest {
        @Test
        void 언팔로우가_정상적으로_작동되는지_확인한다() {
            followService.followMember(팔로워_id, 팔로잉_id);
            final int result = followService.unfollowMember(팔로워_id, 팔로잉_id);

            assertThat(result).isEqualTo(1);
        }

        @Test
        void 팔로잉하지_않은_회원을_언팔로우해도_예외가_발생하지_않는다() {
            final int result = followService.unfollowMember(팔로워_id, 팔로잉_id);
            assertThat(result).isZero();
        }
    }
}
