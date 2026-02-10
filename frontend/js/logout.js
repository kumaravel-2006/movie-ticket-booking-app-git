document.getElementById("logoutBtn").addEventListener("click", () => {
  fetch("http://localhost:8080/api/auth/logout", {
    method: "POST",
    credentials: "include",
  }).then(() => {
    window.location.href = "login.html";
  });
});
