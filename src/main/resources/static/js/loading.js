var masktemp = [
	'<div id="maskloading" class="wrapper nano" style=" position: absolute; display:none;justify-content: center;align-items: center;width: 100%; height: 100%; background-color:#cccccc;">',
    '<div class="la-line-spin-clockwise-fade la-2x">',
       ' <div></div>',
       ' <div></div>',
      '  <div></div>',
      '  <div></div>',
      '  <div></div>',
      '  <div></div>',
      '  <div></div>',
    '    <div></div>',
   ' </div>',
'</div>'

];

document.write(masktemp.join("\n"));

function  loadingOpen() {
     $("#maskloading").css('display','flex');
}

function  loadingClose() {
    $("#maskloading").css('display','none');
}