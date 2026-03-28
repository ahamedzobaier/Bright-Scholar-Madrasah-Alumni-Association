function togglePassword() {
    const passwordField = document.getElementById("passwordField");
    if (passwordField.type === "password") {
        passwordField.type = "text";
    } else {
        passwordField.type = "password";
    }
}

// Add a simple fade-in effect on load
document.addEventListener('DOMContentLoaded', () => {
    const box = document.querySelector('.login-box');
    box.style.opacity = 0;
    box.style.transform = 'translateY(20px)';
    
    setTimeout(() => {
        box.style.transition = 'all 0.6s ease-out';
        box.style.opacity = 1;
        box.style.transform = 'translateY(0)';
    }, 100);
});