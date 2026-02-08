const params = new URLSearchParams(window.location.search);
const movieId = params.get("movieId");

if (!movieId) {
  alert("Invalid movie selection");
  throw new Error("movieId missing");
}

const MOVIE_API = "http://localhost:8080/api/movies/";
const BOOKING_API = "http://localhost:8080/api/bookings";

let price = 150;

// -------- Load Movie --------
fetch(MOVIE_API + movieId)
  .then((res) => {
    if (!res.ok) {
      throw new Error("Movie not found");
    }
    return res.json();
  })
  .then((movie) => {
    document.getElementById("movieName").innerText = movie.name;
    document.getElementById("seatInfo").innerText =
      "Available Seats: " + movie.availableSeats;
  })
  .catch((err) => {
    console.error(err);
    alert("Unable to load movie details");
  });

// -------- Generate Bill --------
function generateBill() {
  const tickets = parseInt(document.getElementById("tickets").value, 10);

  if (isNaN(tickets) || tickets <= 0) {
    alert("Enter valid ticket count");
    return;
  }

  if (tickets > 16) {
    alert("You cannot book more than 16 tickets at a time!");
    return;
  }

  const total = tickets * price;

  document.getElementById("billBox").innerHTML = `
        <h3>Ticket Summary</h3>
        <p>Tickets: ${tickets}</p>
        <p>Price per Ticket: ₹${price}</p>
        <p><strong>Total: ₹${total}</strong></p>
        <button class="primary-btn" onclick="pay()">Confirm Booking</button>
    `;

  document.getElementById("billOverlay").classList.remove("hidden");
}

// -------- Pay --------
function pay() {
  const tickets = parseInt(document.getElementById("tickets").value, 10);

  fetch(BOOKING_API, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      userName: "Guest",
      numberOfTickets: tickets,
      ticketPrice: price,
      movie: { id: parseInt(movieId, 10) },
    }),
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error("Booking failed");
      }
      alert("Booking Confirmed");
      window.location.href = "index.html";
    })
    .catch((err) => {
      console.error(err);
      alert("Booking failed");
    })
    .finally(() => {
      document.getElementById("billOverlay").classList.add("hidden");
    });
}
