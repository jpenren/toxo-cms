/** App functionalities */
$(document).ready(function(){
	$('body').on('hidden.bs.modal', '.modal', function () {
		//fix summernote on modal window
		window.location.href=window.location.href;
	});
});