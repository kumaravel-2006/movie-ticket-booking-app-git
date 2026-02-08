const BOOKING_API = "http://localhost:8080/api/bookings";

fetch(BOOKING_API)
  .then((res) => {
    if (!res.ok) {
      throw new Error("Failed to fetch bookings");
    }
    return res.json();
  })
  .then((data) => {
    const container = document.getElementById("ticketList");
    container.innerHTML = "";

    data.forEach((b) => {
      const div = document.createElement("div");
      div.className = "ticket-card";

      div.innerHTML = `
                <h3>${b.movie.name}</h3>
                <p>Tickets: ${b.numberOfTickets}</p>
                <button onclick="cancelTicket(${b.id})">Cancel Ticket</button>
            `;

      container.appendChild(div);
    });
  })
  .catch((err) => {
    console.error(err);
    document.getElementById("ticketList").innerHTML =
      "<p class='error'>Unable to load tickets</p>";
  });

function cancelTicket(id) {
  fetch(`${BOOKING_API}/${id}`, {
    method: "DELETE",
  })
    .then((res) => {
      if (res.status === 204) {
        alert("Ticket cancelled");
        location.reload();
      } else if (res.status === 404) {
        alert("Ticket not found");
      } else {
        throw new Error("Cancel failed");
      }
    })
    .catch((err) => {
      console.error(err);
      alert("Error cancelling ticket");
    });
}
