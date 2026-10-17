import React, { useState, useEffect } from "react";

const API_URL = "http://localhost:8080/api/reviews";

export default function RecipeReviews({ recipeId }) {
  const [reviews, setReviews] = useState([]);
  const [rating, setRating] = useState(5);
  const [hover, setHover] = useState(0);
  const [reviewerName, setReviewerName] = useState("");
  const [comment, setComment] = useState("");

  const fetchReviews = async () => {
    try {
      const res = await fetch(`${API_URL}/recipe/${recipeId}`);
      if (res.ok) {
        const data = await res.json();
        setReviews(data);
      }
    } catch (err) {
      console.error("Failed to fetch reviews:", err);
    }
  };

  useEffect(() => {
    if (recipeId) fetchReviews();
  }, [recipeId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const newReview = { recipeId, reviewerName, rating, comment };

    try {
      const res = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newReview),
      });
      if (res.ok) {
        setReviewerName("");
        setComment("");
        setRating(5);
        fetchReviews();
      }
    } catch (err) {
      console.error("Error submitting review:", err);
    }
  };

  const averageRating =
    reviews.length > 0
      ? (reviews.reduce((sum, r) => sum + r.rating, 0) / reviews.length).toFixed(1)
      : "No ratings yet";

  return (
    <div style={{ maxWidth: "500px", marginTop: "20px", fontFamily: "sans-serif" }}>
      <h3>Recipe Reviews (Rating: ⭐ {averageRating} / 5)</h3>

      <form onSubmit={handleSubmit} style={{ marginBottom: "20px" }}>
        <div style={{ marginBottom: "10px" }}>
          {[1, 2, 3, 4, 5].map((star) => (
            <button
              type="button"
              key={star}
              onClick={() => setRating(star)}
              onMouseEnter={() => setHover(star)}
              onMouseLeave={() => setHover(rating)}
              style={{
                background: "none",
                border: "none",
                cursor: "pointer",
                fontSize: "24px",
                color: star <= (hover || rating) ? "#ffc107" : "#e4e5e9",
              }}
            >
              ★
            </button>
          ))}
        </div>

        <input
          type="text"
          placeholder="Your Name"
          value={reviewerName}
          onChange={(e) => setReviewerName(e.target.value)}
          required
          style={{ width: "100%", padding: "8px", marginBottom: "8px" }}
        />

        <textarea
          placeholder="Write your review..."
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          required
          rows={3}
          style={{ width: "100%", padding: "8px", marginBottom: "8px" }}
        />

        <button type="submit" style={{ padding: "8px 16px", cursor: "pointer" }}>
          Submit Review
        </button>
      </form>

      <div>
        {reviews.map((r) => (
          <div key={r.id} style={{ borderBottom: "1px solid #ddd", padding: "10px 0" }}>
            <strong>{r.reviewerName}</strong>{" "}
            <span style={{ color: "#ffc107" }}>{"★".repeat(r.rating)}{"☆".repeat(5 - r.rating)}</span>
            <p style={{ margin: "5px 0" }}>{r.comment}</p>
          </div>
        ))}
      </div>
    </div>
  );
}