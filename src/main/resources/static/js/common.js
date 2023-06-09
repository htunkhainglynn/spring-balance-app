document.addEventListener('DOMContentLoaded', () => {
	document.getElementById('signoutLink').addEventListener('click', () => {
		document.getElementById('signoutForm').submit();
	});
	
	Array.from(document.getElementsByClassName('userStatusChangeLink'))
		.forEach((link) => link.addEventListener('click', () => {
			
			document.getElementById('changeUserId').setAttribute('value', link.getAttribute('data-id'))
			document.getElementById('changeUserStatus').setAttribute('value', link.getAttribute('data-status'))
			
			document.getElementById('changeUserName').innerText = link.getAttribute('data-name');
			document.getElementById('changeUserStatusBefore').innerText = link.getAttribute('data-status') ? 'Active' : 'Suspend';
			document.getElementById('changeUserStatusAfter').innerText = link.getAttribute('data-status') ? 'Suspend' : 'Active';
			
			const dialog = new bootstrap.Modal("#userStatusChangeModel");
			dialog.show();
		}));
});
