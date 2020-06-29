$(document).ready(function() {
	$("#createForm").validate({
		rules: {
			computerNameInput: "required"
		},
		messages: {
			computerNameInput: {
				required: "Name required"
			}
		}
	})
});