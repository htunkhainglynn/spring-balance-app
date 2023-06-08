import bootstrap from 'bootstrap';

document.addEventListener('DOMContentLoaded', () => {
	document.getElementById('signoutLink').addEventListener('click', () => {
		document.getElementById('signoutForm').submit();
	})
	
	Array.from(document.getElementsByClassName('userStatusChangeLink'))
		.forEach((link) => link.addEventListener('click', () => {
			const dialog = new bootstrap.Modal("#userStatusChangeLink");
			dialog.show();
		}))
})