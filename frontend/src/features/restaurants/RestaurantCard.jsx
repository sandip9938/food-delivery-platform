const cuisineStyles = {
  Indian: {
    icon: "🍛",
    color: "spice",
    image: "photo-1585937421612-70a008356fbe",
  },
  Italian: {
    icon: "🍕",
    color: "tomato",
    image: "photo-1574071318508-1cdbab80d002",
  },
  Healthy: {
    icon: "🥗",
    color: "leaf",
    image: "photo-1512621776951-a57141f2eefd",
  },
  Japanese: {
    icon: "🍣",
    color: "berry",
    image: "photo-1579871494447-9811cf80d66c",
  },
  Mexican: {
    icon: "🌮",
    color: "sunset",
    image: "photo-1552332386-f8dd00dc2f85",
  },
};

export default function RestaurantCard({ restaurant, onSelect }) {
  const style = cuisineStyles[restaurant.cuisineType] ?? {
    icon: "🍽️",
    color: "tomato",
    image: "photo-1546069901-ba9599a7e63c",
  };
  const image = restaurant.imageUrl
    || `https://images.unsplash.com/${style.image}?auto=format&fit=crop&w=900&q=82`;

  return (
    <article className="restaurant-card">
      <button
        className={`restaurant-art ${style.color}`}
        type="button"
        onClick={() => onSelect(restaurant)}
        aria-label={`View ${restaurant.name} menu`}
      >
        <img
          src={image}
          alt=""
          loading="lazy"
          onError={(event) => {
            event.currentTarget.style.display = "none";
          }}
        />
        <span className="restaurant-art-fallback" aria-hidden="true">{style.icon}</span>
        <span className="art-label">{restaurant.cuisineType}</span>
      </button>
      <div className="restaurant-card-body">
        <div className="restaurant-heading">
          <h3>{restaurant.name}</h3>
          <span className="rating"><span aria-hidden="true">★</span> 4.8</span>
        </div>
        <p className="restaurant-description">
          {restaurant.description || `${restaurant.cuisineType} favorites, made fresh.`}
        </p>
        <div className="restaurant-meta">
          <span>⌖ {restaurant.city}</span>
          <span className="delivery-time">25–35 min</span>
        </div>
        <button className="text-button" type="button" onClick={() => onSelect(restaurant)}>
          Explore menu <span aria-hidden="true">→</span>
        </button>
      </div>
    </article>
  );
}
