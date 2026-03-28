/* --- UU Transport Hero Slider Logic --- */

let currentSlide = 0;
let slideInterval;

function showSlide(index) {
    const slides = document.querySelectorAll('.slide');
    if (slides.length === 0) return;

    // Reset loop if index is out of bounds
    if (index >= slides.length) currentSlide = 0;
    else if (index < 0) currentSlide = slides.length - 1;
    else currentSlide = index;

    // Hide all slides and show active one
    slides.forEach(slide => {
        slide.classList.remove('active');
    });
    
    slides[currentSlide].classList.add('active');
}

function changeSlide(direction) {
    // Reset timer when user manually clicks
    clearInterval(slideInterval);
    startAutoSlide();
    
    showSlide(currentSlide + direction);
}

function startAutoSlide() {
    slideInterval = setInterval(() => {
        showSlide(currentSlide + 1);
    }, 5000); // 5 seconds per slide
}

// Initialize on load
document.addEventListener("DOMContentLoaded", () => {
    showSlide(currentSlide);
    startAutoSlide();
});