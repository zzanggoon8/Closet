package com.ssu.project.service.review;

import com.ssu.project.domain.item.Item;
import com.ssu.project.domain.member.Member;
import com.ssu.project.domain.review.Review;
import com.ssu.project.domain.review.ReviewRepository;
import com.ssu.project.dto.review.ReviewForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public void createReview(Member member, Item item, ReviewForm reviewForm) {
        Review review = Review.builder()
                .item(item)
                .title(reviewForm.getTitle())
                .content(reviewForm.getContents())
                .member(member)
                .createDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        reviewRepository.save(review);
    }

    public void deleteReview(long reviewId) {
        Review review = reviewRepository.findById(reviewId);

        if (reviewRepository.findById(reviewId)==null) {
            new IllegalArgumentException("해당 리뷰가 없습니다. Id "+ reviewId);
        }
        reviewRepository.delete(review);
    }

    public void updateReview(Review review, ReviewForm reviewForm) {

        Optional<Review> updateReview = reviewRepository.findById(review.getId());

        if (updateReview!=null) {
            review.setTitle(reviewForm.getTitle());
            review.setContent(reviewForm.getContents());
            review.setUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        reviewRepository.save(review);
    }


    public Optional<Review> findById(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);

        if (review==null) {
            new IllegalArgumentException("해당 리뷰가 없습니다. Id "+ reviewId);
        }
        return review;
    }
}
