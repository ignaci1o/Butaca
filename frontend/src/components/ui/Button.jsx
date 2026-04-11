function Button({ children, variant = "primary", fullWidth = false, ...props }) {
  const className = ["button", `button-${variant}`, fullWidth ? "button-full" : ""]
    .filter(Boolean)
    .join(" ");

  return (
    <button className={className} {...props}>
      {children}
    </button>
  );
}

export default Button;
