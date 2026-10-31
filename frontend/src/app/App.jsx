import { useCallback, useEffect, useRef, useState } from "react";
import CartPanel from "../features/cart/CartPanel.jsx";
import MenuDialog from "../features/menu/MenuDialog.jsx";
import RestaurantCard from "../features/restaurants/RestaurantCard.jsx";
import { getHealth, getRestaurants } from "../services/api.js";

const cuisines = ["All cuisines", "Indian", "Italian", "Healthy", "Japanese", "Mexican"];

export default function App() {
  const [restaurants, setRestaurants] = useState([]);
  const [search, setSearch] = useState("");
  const [searchInput, setSearchInput] = useState("");
  const [city, setCity] = useState("");
  const [cuisine, setCuisine] = useState("All cuisines");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [loadingMore, setLoadingMore] = useState(false);
  const [error, setError] = useState("");
  const [backendOnline, setBackendOnline] = useState(false);
  const [selectedRestaurant, setSelectedRestaurant] = useState(null);
  const [cart, setCart] = useState([]);
  const [cartOpen, setCartOpen] = useState(false);
  const [toast, setToast] = useState("");
  const searchTimer = useRef(null);
  const toastTimer = useRef(null);

  const loadRestaurants = useCallback(async (nextPage = 0, append = false, signal) => {
    if (append) setLoadingMore(true);
    else setLoading(true);
    setError("");
    try {
      const result = await getRestaurants({
        search,
        city,
        cuisine: cuisine === "All cuisines" ? "" : cuisine,
        page: nextPage,
      }, signal);
      setRestaurants((current) => append ? [...current, ...result.content] : result.content);
      setPage(result.number);
      setTotalPages(result.totalPages);
      setBackendOnline(true);
    } catch (requestError) {
      if (signal?.aborted) return;
      setError(requestError.message);
      setBackendOnline(false);
      if (!append) setRestaurants([]);
    } finally {
      if (!signal?.aborted) {
        setLoading(false);
        setLoadingMore(false);
      }
    }
  }, [city, cuisine, search]);

  useEffect(() => {
    const controller = new AbortController();
    loadRestaurants(0, false, controller.signal);
    return () => controller.abort();
  }, [loadRestaurants]);

  useEffect(() => () => {
    window.clearTimeout(searchTimer.current);
    window.clearTimeout(toastTimer.current);
  }, []);

  function updateSearch(value) {
    setSearchInput(value);
    window.clearTimeout(searchTimer.current);
    searchTimer.current = window.setTimeout(() => setSearch(value), 250);
  }

  async function checkConnection() {
    try {
      await getHealth();
      setBackendOnline(true);
      await loadRestaurants(0);
    } catch (requestError) {
      setBackendOnline(false);
      setError(requestError.message);
    }
  }

  function addToCart(menuItem, restaurant) {
    setCart((current) => {
      const existing = current.find((item) => item.id === menuItem.id);
      if (existing) {
        return current.map((item) => item.id === menuItem.id
          ? { ...item, quantity: item.quantity + 1 }
          : item);
      }
      return [...current, {
        id: menuItem.id,
        name: menuItem.name,
        price: menuItem.price,
        restaurantName: restaurant.name,
        quantity: 1,
      }];
    });
    setToast(`${menuItem.name} added to your basket`);
    window.clearTimeout(toastTimer.current);
    toastTimer.current = window.setTimeout(() => setToast(""), 2400);
  }

  function changeQuantity(item, change) {
    setCart((current) => current
      .map((cartItem) => cartItem.id === item.id
        ? { ...cartItem, quantity: cartItem.quantity + change }
        : cartItem)
      .filter((cartItem) => cartItem.quantity > 0));
  }

  const cartCount = cart.reduce((total, item) => total + item.quantity, 0);

  return (
    <div className="app-shell">
      <header className="site-header">
        <a className="brand" href="#" aria-label="Biteful home">
          <span className="brand-mark" aria-hidden="true">b.</span>
          <span>biteful<span className="brand-dot">.</span></span>
        </a>
        <div className="header-location"><span aria-hidden="true">⌖</span><span>Delivering around</span><strong>{city || "Your city"}</strong></div>
        <div className="header-actions">
          <button
            className={`connection ${backendOnline ? "connected" : "disconnected"}`}
            type="button"
            onClick={checkConnection}
            aria-label="Check backend connection"
          >
            <i /> {backendOnline ? "Live menu" : "API offline"}
          </button>
          <button className="basket-button" type="button" onClick={() => setCartOpen(true)}>
            <span aria-hidden="true">♧</span> Basket
            {cartCount > 0 && <span className="basket-count">{cartCount}</span>}
          </button>
        </div>
      </header>

      <main>
        <section className="hero">
          <div className="hero-copy">
            <p className="eyebrow"><span className="hero-sparkle">✳</span> GOOD FOOD, GOOD MOOD</p>
            <h1>Cravings?<br /><span>Consider it</span><br />handled.</h1>
            <p className="hero-description">The good stuff from the spots you love, brought right to your door.</p>
            <a className="hero-link" href="#restaurants">Find your next favorite <span aria-hidden="true">↓</span></a>
          </div>
          <div className="hero-art" aria-hidden="true">
            <div className="hero-sun" />
            <span className="hero-food">🥡</span>
            <span className="hero-leaf leaf-one">✳</span>
            <span className="hero-leaf leaf-two">✳</span>
            <span className="hero-note">made with<br />a little love <b>♥</b></span>
            <span className="hero-orbit">FRESH · LOCAL · DELICIOUS ·</span>
          </div>
        </section>

        <section className="discover-section" id="restaurants">
          <div className="section-heading">
            <div>
              <p className="eyebrow">YOUR NEIGHBORHOOD, ON A PLATE</p>
              <h2>Find your kind of <span>delicious.</span></h2>
            </div>
            <p className="section-aside">Good things are just around the corner <span aria-hidden="true">↘</span></p>
          </div>

          <div className="search-panel">
            <label className="search-field">
              <span aria-hidden="true">⌕</span>
              <input
                type="search"
                placeholder="Dishes, restaurants, cuisines…"
                value={searchInput}
                onChange={(event) => updateSearch(event.target.value)}
                aria-label="Search restaurants and cuisines"
              />
            </label>
            <label className="city-field">
              <span aria-hidden="true">⌖</span>
              <input
                type="search"
                placeholder="Your city"
                value={city}
                onChange={(event) => setCity(event.target.value)}
                aria-label="Filter by city"
              />
            </label>
            <span className="search-caption">MADE FOR YOUR NEIGHBORHOOD</span>
          </div>

          <div className="cuisine-tabs" role="group" aria-label="Filter by cuisine">
            {cuisines.map((item) => (
              <button
                key={item}
                className={`cuisine-tab ${cuisine === item ? "active" : ""}`}
                type="button"
                onClick={() => setCuisine(item)}
              >
                {item !== "All cuisines" && <span aria-hidden="true">
                  {{ Indian: "🍛", Italian: "🍕", Healthy: "🥗", Japanese: "🍣", Mexican: "🌮" }[item]}
                </span>}
                {item}
              </button>
            ))}
          </div>

          <div className="results-heading">
            <h3>{cuisine === "All cuisines" ? "The neighborhood favorites" : `${cuisine}, your way`}</h3>
            {!loading && !error && <span>{restaurants.length} delicious {restaurants.length === 1 ? "spot" : "spots"}</span>}
          </div>

          {error && (
            <div className="notice error-notice" role="alert">
              <span>{error}</span>
              <button type="button" onClick={checkConnection}>Try again</button>
            </div>
          )}
          {loading && <div className="loading-state"><span className="spinner" /> Finding your favorites…</div>}
          {!loading && !error && restaurants.length === 0 && (
            <div className="empty-state">
              <span aria-hidden="true">🍽️</span>
              <h3>No spots found just yet</h3>
              <p>Try a different search or city. New favorites are always popping up.</p>
              <button className="text-button" type="button" onClick={() => {
                window.clearTimeout(searchTimer.current);
                setSearchInput("");
                setSearch("");
                setCuisine("All cuisines");
                setCity("");
              }}>
                Clear filters <span aria-hidden="true">→</span>
              </button>
            </div>
          )}
          {!loading && restaurants.length > 0 && (
            <div className="restaurant-grid">
              {restaurants.map((restaurant) => (
                <RestaurantCard
                  key={restaurant.id}
                  restaurant={restaurant}
                  onSelect={setSelectedRestaurant}
                />
              ))}
            </div>
          )}
          {!loading && !error && page + 1 < totalPages && (
            <button
              className="load-more"
              type="button"
              disabled={loadingMore}
              onClick={() => loadRestaurants(page + 1, true)}
            >
              {loadingMore ? "Loading…" : "A few more spots"} <span aria-hidden="true">↓</span>
            </button>
          )}
        </section>

        <section className="promise-strip" aria-label="Our promise">
          <span><b>✳</b> Local love, delivered</span>
          <span><b>✳</b> Good food, no fuss</span>
          <span><b>✳</b> Your neighborhood, better</span>
        </section>
      </main>

      <footer className="site-footer">
        <a className="brand" href="#"><span className="brand-mark">b.</span><span>biteful<span className="brand-dot">.</span></span></a>
        <span>A little joy, delivered.</span>
        <span className="footer-status"><i /> {backendOnline ? "Connected to the kitchen" : "Kitchen offline"}</span>
      </footer>

      {selectedRestaurant && (
        <MenuDialog
          restaurant={selectedRestaurant}
          onClose={() => setSelectedRestaurant(null)}
          onAddToCart={addToCart}
        />
      )}
      {cartOpen && <CartPanel items={cart} onChangeQuantity={changeQuantity} onClose={() => setCartOpen(false)} />}
      {toast && <div className="toast" role="status"><span>✓</span> {toast}</div>}
    </div>
  );
}
