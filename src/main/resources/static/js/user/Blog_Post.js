function toggleBlogForm() {
    const modal = document.getElementById("blogFormContainer");
    if (modal.style.display === "block") {
        modal.style.display = "none";
    } else {
        modal.style.display = "block";
    }
}

// Close modal if user clicks outside of it
window.onclick = function(event) {
    const modal = document.getElementById("blogFormContainer");
    if (event.target == modal) {
        modal.style.display = "none";
    }
}