// public/js/rating.js
document.addEventListener('DOMContentLoaded', function() {
    const ratingStars = document.querySelectorAll('.rating input');
    const selectedRating = document.getElementById('ratingValue');
    const pharmacieId = document.querySelector('.rating').getAttribute('data-pharmacie-id');

    ratingStars.forEach((star) => {
        star.addEventListener('click', () => {
            const rating = star.value;
            selectedRating.textContent = rating;

            // Utilisez l'ID de la pharmacie pour envoyer la note au serveur (AJAX)
            fetch(`/rate/${pharmacieId}/${rating}`, {
                method: 'POST',
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    console.log('Note mise à jour avec succès');
                } else {
                    console.error('Erreur lors de la mise à jour de la note');
                }
            })
            .catch(error => console.error('Erreur lors de la requête AJAX :', error));
        });
    });
});