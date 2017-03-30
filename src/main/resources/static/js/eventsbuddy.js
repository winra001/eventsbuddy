$(document).ready(function() {
	$("#signupForm").validate({
		rules: {
			email: {
				required: true,
				email: true
			},
			firstName: {
				required: true
			},
			lastName: {
				required: true
			},
			password: {
				required: true
			},
			confirmPassword: {
				required: true,
				equalTo: "#password"
			}
		},
		messages: {
			username: "Email is required",
			firstName: "First Name is required",
			lastName: "Last Name is required",
			password: "Password is required",
			confirmPassword: {
				required: "Confirm Password is required",
				equalTo: "Plase enter the same password as above"
			}
		},
		errorPlacement: function( error, element ) {
			error.insertAfter( element.parent() );
		}
	});
	
	$("#importAttendancesForm").validate({
		rules: {
			file: "required"
		},
		messages: {
			file: "Please choose a file to import attendances"
		},
		errorPlacement: function( error, element ) {
			error.insertAfter( element.parent() );
		}
	});

	$("#importProgramsForm").validate({
		rules: {
			file: "required"
		},
		messages: {
			file: "Please choose a file to import programs"
		},
		errorPlacement: function( error, element ) {
			error.insertAfter( element.parent() );
		}
	});
});