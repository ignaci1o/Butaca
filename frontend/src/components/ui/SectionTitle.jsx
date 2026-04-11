function SectionTitle({ eyebrow, title, text }) {
  return (
    <div className="section-title">
      {eyebrow ? <p className="section-eyebrow">{eyebrow}</p> : null}
      <h2>{title}</h2>
      {text ? <p className="section-text">{text}</p> : null}
    </div>
  );
}

export default SectionTitle;
