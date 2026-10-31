import { useEffect, useState } from "react";
import { getRestaurantMenu } from "../../services/api.js";
import { formatPrice } from "../../services/formatPrice.js";

export default function MenuDialog({ restaurant, onClose, onAddToCart }) {
  const [menu, setMenu] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const controller = new AbortController();
    setLoading(true);
    setError("");

    getRestaurantMenu(restaurant.id, controller.signal)
      .then(setMenu)
      .catch((requestError) => {
        if (!controller.signal.aborted) setError(requestError.message);
      })
      .finally(() => {
        if (!controller.signal.aborted) setLoading(false);
      });

    return () => controller.abort();
  }, [restaurant.id]);

  return (
    <div className="overlay" onMouseDown={onClose}>
      <section
        className="menu-dialog"
        role="dialog"
        aria-modal="true"
        aria-labelledby="menu-title"
        onMouseDown={(event) => event.stopPropagation()}
      >
        <div className="menu-banner">
          <button className="icon-button menu-close" type="button" onClick={onClose} aria-label="Close menu">
            ×
          </button>
          <span className="menu-banner-icon" aria-hidden="true">🍴</span>
          <p className="eyebrow">{restaurant.cuisineType} · {restaurant.city}</p>
          <h2 id="menu-title">{restaurant.name}</h2>
          <p>{restaurant.description || restaurant.address}</p>
        </div>
        <div className="menu-content">
          <div className="menu-section-heading">
            <div>
              <p className="eyebrow">MADE FRESH FOR YOU</p>
              <h3>Popular picks</h3>
            </div>
            <span>{menu.length} items</span>
          </div>
          {loading && <div className="loading-state"><span className="spinner" /> Loading menu…</div>}
          {error && <div className="notice error-notice" role="alert">{error}</div>}
          {!loading && !error && menu.length === 0 && (
            <div className="empty-menu">
              <span aria-hidden="true">🍳</span>
              <p>This restaurant is getting its menu ready. Check back soon.</p>
            </div>
          )}
          <div className="menu-list">
            {menu.map((item) => (
              <article className="menu-item" key={item.id}>
                <div className="menu-item-info">
                  <span className={`food-tag ${item.vegetarian ? "veg" : ""}`}>
                    {item.vegetarian ? "VEG" : "CHEF'S PICK"}
                  </span>
                  <h4>{item.name}</h4>
                  <p>{item.description || item.category}</p>
                  <strong>{formatPrice(item.price)}</strong>
                </div>
                <button
                  className="add-button"
                  type="button"
                  onClick={() => onAddToCart(item, restaurant)}
                  aria-label={`Add ${item.name} to basket`}
                >+</button>
              </article>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
}
