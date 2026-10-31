import { formatPrice } from "../../services/formatPrice.js";

export default function CartPanel({ items, onChangeQuantity, onClose }) {
  const itemCount = items.reduce((total, item) => total + item.quantity, 0);
  const subtotal = items.reduce(
    (total, item) => total + Number(item.price) * item.quantity,
    0,
  );

  return (
    <div className="overlay" onMouseDown={onClose}>
      <aside
        className="cart-panel"
        aria-labelledby="cart-title"
        onMouseDown={(event) => event.stopPropagation()}
      >
        <div className="panel-heading">
          <div>
            <p className="eyebrow">YOUR ORDER</p>
            <h2 id="cart-title">Your basket <span>({itemCount})</span></h2>
          </div>
          <button className="icon-button" type="button" onClick={onClose} aria-label="Close basket">
            ×
          </button>
        </div>
        {items.length === 0 ? (
          <div className="empty-cart">
            <span aria-hidden="true">🧺</span>
            <h3>Your basket is waiting</h3>
            <p>Find something delicious and add it here.</p>
          </div>
        ) : (
          <>
            <div className="cart-items">
              {items.map((item) => (
                <div className="cart-item" key={item.id}>
                  <div className="cart-item-icon" aria-hidden="true">🍽️</div>
                  <div className="cart-item-info">
                    <strong>{item.name}</strong>
                    <span>{item.restaurantName}</span>
                    <b>{formatPrice(Number(item.price) * item.quantity)}</b>
                  </div>
                  <div className="quantity-control">
                    <button
                      type="button"
                      onClick={() => onChangeQuantity(item, -1)}
                      aria-label={`Remove one ${item.name}`}
                    >−</button>
                    <span>{item.quantity}</span>
                    <button
                      type="button"
                      onClick={() => onChangeQuantity(item, 1)}
                      aria-label={`Add one ${item.name}`}
                    >+</button>
                  </div>
                </div>
              ))}
            </div>
            <div className="cart-summary">
              <div><span>Subtotal</span><strong>{formatPrice(subtotal)}</strong></div>
              <div><span>Delivery</span><span>Calculated at checkout</span></div>
              <button type="button" className="checkout-button" disabled>
                Checkout coming soon
              </button>
              <p>Checkout becomes available when orders are connected.</p>
            </div>
          </>
        )}
      </aside>
    </div>
  );
}
