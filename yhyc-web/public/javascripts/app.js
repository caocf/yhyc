$(function() {
	$('a.delete').click(function(e) {
		
		BootstrapDialog.confirm('Are you sure to delete this?', function(result){
            if(result) {
            	var href = $('a.delete').attr('href')
    			$.ajax({
    				type: 'DELETE',
    				url: href,
    				success: function() {
    					document.location.reload()
    				}
    			})
            }else {
            }
        });
		
		e.preventDefault();
		return false
	})
});

$('.dropdown-menu li').click(function(e){
	e.preventDefault();
	var selected = $(this).text();
	$('#category').text(selected);
	$('#category').val(selected);
});