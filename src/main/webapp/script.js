/**
 * Function to load external HTML files into placeholders
 * @param {string} id - The ID of the element to inject HTML into
 * @param {string} file - The path to the .html file
 */
function loadComponent(id, file) {
    fetch(file)
        .then(response => {
            if (!response.ok) throw new Error(`Could not find ${file}`);
            return response.text();
        })
        .then(data => {
            document.getElementById(id).innerHTML = data;
        })
        .catch(error => console.error('Error loading component:', error));
}

// Initialize the components when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", () => {
    loadComponent('header-placeholder', 'header.html');
    loadComponent('footer-placeholder', 'footer.html');
});