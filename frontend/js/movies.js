const MOVIE_API = "http://localhost:8080/api/movies";

fetch(MOVIE_API)
  .then((res) => {
    if (!res.ok) {
      throw new Error("Failed to fetch movies");
    }
    return res.json();
  })
  .then((movies) => {
    const container = document.getElementById("movieList");
    container.innerHTML = ""; // defensive clear

    movies.forEach((movie) => {
      const card = document.createElement("div");
      card.className = "card";
      card.innerHTML = `
                <h3>${movie.name}</h3>
                <p>Duration: ${movie.duration} mins</p>
                <p>Rating: ${movie.rating}</p>
                <button onclick="book(${movie.id})">Book Now</button>
            `;
      container.appendChild(card);
    });
  })
  .catch((err) => {
    console.error(err);
    document.getElementById("movieList").innerHTML =
      "<p class='error'>Unable to load movies. Please try again later.</p>";
  });

function book(id) {
  window.location.href = `booking.html?movieId=${id}`;
}
