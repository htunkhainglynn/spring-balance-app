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
	const pageSizeChangeSelect = document.getElementById('pageSizeChangeSelect');
	if (pageSizeChangeSelect) {
		pageSizeChangeSelect.addEventListener('change', () => {
						
			const formId = pageSizeChangeSelect.getAttribute('data-form-id');
			const value = pageSizeChangeSelect.value;
			const form = document.getElementById(formId);
			const sizeInput = document.createElement('input');
			sizeInput.setAttribute('type', 'hidden');
			sizeInput.setAttribute('name', 'size');
			sizeInput.setAttribute('value', value);
			form.appendChild(sizeInput);
			form.submit();
			
		})
	}
});
