import EventCard from "./EventCard";

function EventGrid({ events }) {
  if (!events.length) {
    return <p className="empty-state">No hay eventos que coincidan con tu filtro actual.</p>;
  }

  return (
    <div className="event-grid">
      {events.map((event) => (
        <EventCard key={event.id} event={event} />
      ))}
    </div>
  );
}

export default EventGrid;
