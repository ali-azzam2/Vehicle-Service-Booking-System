// Session handling functions
async function checkSession() {
    try {
        const response = await fetch('/api/auth/current-user', {
            credentials: 'include',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (response.ok) {
            const user = await response.json();
            updateUIForUser(user);
            return user;
        } else {
            // If not authenticated, redirect to login
            if (!window.location.pathname.includes('login.html') && 
                !window.location.pathname.includes('register.html')) {
                window.location.href = '/login.html';
            }
            return null;
        }
    } catch (error) {
        console.error('Error checking session:', error);
        return null;
    }
}

function updateUIForUser(user) {
    const userOnlyElements = document.querySelectorAll('.user-only');
    const mechanicOnlyElements = document.querySelectorAll('.mechanic-only');
    
    // Hide all role-specific elements first
    userOnlyElements.forEach(el => el.style.display = 'none');
    mechanicOnlyElements.forEach(el => el.style.display = 'none');

    // Show elements based on role
    if (user.role === 'USER') {
        userOnlyElements.forEach(el => el.style.display = 'block');
    } else if (user.role === 'MECHANIC') {
        mechanicOnlyElements.forEach(el => el.style.display = 'block');
    }

    // Update navigation based on role
    const navLinks = document.querySelector('.nav-links');
    if (navLinks) {
        if (user.role === 'USER') {
            navLinks.innerHTML = `
                <a href="/services.html">Services</a>
                <a href="/book.html">Book Service</a>
                <a href="/history.html">History</a>
                <a href="/profile.html">Profile</a>
                <a href="#" onclick="logout()">Logout</a>
            `;
        } else if (user.role === 'MECHANIC') {
            navLinks.innerHTML = `
                <a href="/mechanic.html">Dashboard</a>
                <a href="/profile.html">Profile</a>
                <a href="#" onclick="logout()">Logout</a>
            `;
        }
    }
}

async function logout() {
    try {
        const response = await fetch('/api/auth/logout', {
            method: 'POST',
            credentials: 'include'
        });

        if (response.ok) {
            window.location.href = '/login.html';
        }
    } catch (error) {
        console.error('Error during logout:', error);
    }
}

// Check session when page loads
document.addEventListener('DOMContentLoaded', checkSession); 