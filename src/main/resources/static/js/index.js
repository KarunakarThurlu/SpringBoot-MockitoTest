//Send multipart form data using jquery on doucument ready and download excel file
$(document).ready(function() {
  $("#uploadbtn").on('click', function(e) {
   e.preventDefault();
   var validations= validateForm();
   if(validations.isValid){
   //remove error message if file is selected
   $("#fileName").remove();
   $("#uploadbtn").prop("disabled", true);
   var file = $("#file")[0].files[0];
   var data=new FormData();
   data.append('file',file);
   var request = new XMLHttpRequest();
   request.open('POST', '/api/upload', true);
   contentType: "application/json; charset=utf-8",
   request.responseType = 'blob';
   request.onload = function(e) {
       if (this.status === 200) {
           var fileName = request.getResponseHeader("Content-Disposition");
           var blob = this.response;
           if(window.navigator.msSaveOrOpenBlob) {
               window.navigator.msSaveBlob(blob, foo.xlsx);
           }else{
               var downloadLink = window.document.createElement('a');
               var contentTypeHeader = request.getResponseHeader("Content-Type");
               downloadLink.href = window.URL.createObjectURL(new Blob([blob], { type: contentTypeHeader }));
               downloadLink.download = fileName;
               document.body.appendChild(downloadLink);
               downloadLink.click();
               document.body.removeChild(downloadLink);
               $("#uploadbtn").prop("disabled", false);
              }
          }
      };
       request.send(data);
      }else{
        //clear previous error message and add new error message
        $("#fileName").empty();
        $("#file").after("</br><span id='fileName' style='color:red;'>"+validations.message+"</span>");
        $("#uploadbtn").prop("disabled", false);
      }
  });
});

function validateForm() {
  var errorObj={isValid:true,message:""};
  var x = $("#file")[0].files[0]!==undefined?$("#file")[0].files[0].name:"";
  if (x == "") {
    errorObj["message"] = "File must be selected";
    errorObj["isValid"] = false;
    return errorObj;
  }
  if(x!==""){
    //allow only .xlsx  and .xls file
    if(x.substring(x.lastIndexOf('.') + 1) != 'xlsx' && x.substring(x.lastIndexOf('.') + 1) != 'xls') {
      errorObj["message"] ="Only .xlsx and .xls file allowed";
      errorObj["isValid"] = false;
       return errorObj;
    }
    //special characters are not allowed in file name and ignore file extension .xlsx and .xls
    if(/[^a-zA-Z0-9_\s]/.test(x.substring(0,x.lastIndexOf('.')))){
      errorObj["message"] ="File name should not contain special characters";
      errorObj["isValid"] = false;
       return errorObj;
    }
  }
  return errorObj;
}

