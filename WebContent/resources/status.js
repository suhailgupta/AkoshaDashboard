
//to show loader
function showLoader(){

	document.getElementById('LoadingBar').style.display = 'block';

}
//to hide loader
function hideLoader()
{

	document.getElementById('LoadingBar').style.display = 'none';
}

var page_id="";
//function to get pagetoken for commenting on behalf of page admin
function getpagetoken(e){
	var brandname = $('#brand').val();
	var id=e.id;
	var brandid=page_id;	
	$.ajax({  
		type: "GET",  
		url:"https://graph.facebook.com/"+ facebook_id+"/accounts?access_token="+access_token,
		success: function(response){
			var jsonpageobject=JSON.stringify(response);
			var jsonpageresponse=JSON.parse(jsonpageobject);

			for(var i =0 ; i < jsonpageresponse.data.length; i++){

				if(jsonpageresponse.data[i].id==brandid){
					pagetoken=jsonpageresponse.data[i].access_token;
					break;
				}else
					pagetoken=access_token;
			}

			commentusingpagetoken(pagetoken,id);

			hideLoader();
		},
		error: function(e){  

			console.log("Facebook data could not be retrieved.  Failed with a status of " + status);
		} 
	});


}
//function for commenting on behalf of page admin.
function commentusingpagetoken(token,e){
	var id=e;
	var textid="#text"+id;
	var comment=$(textid).val();
	$.ajax({  
		type: "POST",  
		url: "https://graph.facebook.com/"+id+"/comments?access_token=" +token, 
		data:"message="+comment,
		success: function(response){
		//	alert("success");
			//	alert(response.id);
			alert("comment posted on facebook");
		},
		error: function(e){  

			console.log("Facebook data could not be retrieved.  Failed with a status of " + status);
		} 
	});


}

//timer for checking latest feeds
var stop=undefined;
function getposttimer(){

	var url=contexPath + "/latestposts";

	if(stop==undefined)
	{
		console.log("Starting Timer");
		stop= window.setInterval(function(){doAjaxGet(undefined,url);}, 1000*60);
	}else
	{
		//nothing
	}

}
//stoping timer whenever user is in comment box.
function timerstop(){
	clearInterval(stop);
	stop=undefined;
	console.log("Timer Stopped");

}
//ajax method for getting feeds.
function doAjaxGet(bool,ajaxurl) {  
	// get the form values
	//value of the brand name which user entered in textbox
	var name = $('#brand').val();
	// checking bool for whether request is for new page or same page but old feeds.
	//bool=false means request is for new page
	if(bool==undefined)
	{

		bool=false;
	}
	// isLatest to check whether there are any latest feeds after timer runs.
	var isLatest=false;
	//ajaxurl is just used for changing url based on latest and same page old feeds.
	//ajaxurl=undefined means currently it will fetch feeds for same page.
	//latest ajaxurl is getting passed from getposttimer method
	//every 2 minutes ajaxurl will get change and request will go for latest posts
	if(ajaxurl==undefined)
	{
		ajaxurl=contexPath + "/getposts";
	}
	else
	{
		isLatest=true;
	}
	// showing loader when ajax request is happening.
	showLoader();
	$.ajax({  
		type: "GET",  
		url: ajaxurl, 
		data: "name=" + name+"&next="+bool,
		success: function(response){
			removeErrorMessages();
//			on success creating dynamic html page depending on number of feeds.
			var jsonobject=JSON.stringify(response);
			var jsonresponse=JSON.parse(jsonobject);

			var	statuses=document.getElementById("statusandcomments");

//			if request went to old next feeds, then Next hyperlink should be moved to last of next old feeds,
			//	i have to show to next old feeds.
			if(statuses.lastChild!=undefined && statuses.lastChild.tagName=='DIV')
			{

				statuses.removeChild(statuses.lastChild);
			}

			var oldStatuses=statuses.innerHTML;
			statuses=statuses.innerHTML;
			statuses="";

			if(jsonresponse[0] !=undefined){
				if(jsonresponse[0].to[0] !=undefined)
					pageid=jsonresponse[0].to[0].id;
				else
					page_id=jsonresponse[0].from.id;

			}			
			//alert(page_id);
			for(var i =0 ; i < jsonresponse.length ; i++){


				if(jsonresponse[i].message !=null ){

					statuses += "<br><div class='status'><b>Status</b> : " +"<b>"+jsonresponse[i].from.name +"</b>: "+ jsonresponse[i].message ;
					statuses+='<br/><b>Comments:</b>';

					for(var j =0 ; j < jsonresponse[i].comments.length ; j++){
						{
							if(jsonresponse[i].comments[j]!=undefined){

								statuses+="<div class='status_comment'>";
								statuses+='<b>'+jsonresponse[i].comments[j].from.name+'</b>';
								statuses+=": <i>"+jsonresponse[i].comments[j].message;
								statuses+="</i></div>";
							}
						}
					}
					statuses+="<br><input type='text'id=text"+jsonresponse[i].id+" onfocus=timerstop() onblur=getposttimer()></input></br>";
					statuses+="<input type='button' id="+jsonresponse[i].id+" value='Addcomments' onclick=getpagetoken(this);></input></div>";

				}
			}


//			if latest feeds are there , then we have to show latest feeds on the top of already displyed feeds.
			if(isLatest)
			{

				statuses=statuses+oldStatuses;

			}
			// if user is requesting old feeds , then i have to show old feeds at the end.	
			else if( bool!=false)
			{
				statuses=oldStatuses+statuses;
			}

			statuses+= '<div id="NextAndError"><a class="nextButton" href="#" onclick="return doAjaxGet(true);" >Next</a></div>';
			$('#statusandcomments').html(statuses);


			console.log("Successfully retrieved Facebook data");
			//  console.dir(data);

			hideLoader();
		},  
		error: function(e){  

			hideLoader();

			var errors=document.getElementById("errors");
			errors.style.display="block";
		} 
	}); 
	return false;
}  

//for removing error messages
function removeErrorMessages()
{

	var errors=document.getElementById("errors");
	errors.style.display="none";
}