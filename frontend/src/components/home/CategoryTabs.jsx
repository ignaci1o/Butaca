function CategoryTabs({ categories, activeCategory, onChange }) {
  return (
    <div className="category-tabs">
      {categories.map((category) => (
        <button
          key={category}
          type="button"
          className={activeCategory === category ? "category-tab active" : "category-tab"}
          onClick={() => onChange(category)}
        >
          {category}
        </button>
      ))}
    </div>
  );
}

export default CategoryTabs;
