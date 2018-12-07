
function show(totalCounts,from,to,currPage,pageSize,pageCount,parentId) {
    document.getElementById("turnPage").innerHTML = '<div class="col-xs-6"> ' +
        '<div class="dataTables_info" id="dynamic-table_info" role="status" aria-live="polite">总共 '+totalCounts+' 中的 '+from+' ~ '+to+' </div> </div> ' +
        '<div class="col-xs-6"> <div class="dataTables_paginate paging_simple_numbers" id="dynamic-table_paginate"> ' +
        '<ul class="pagination"> ' +
        '<li class="paginate_button previous {{^firstUrl}}disabled{{/firstUrl}}" aria-controls="dynamic-table" tabindex="0"> ' +
        '<a href="#" onclick=loadUsers(1,'+pageSize+','+parentId+') class="page-action">首页</a> </li> ' +
        '<li class="paginate_button {{^beforeUrl}}disabled{{/beforeUrl}}" aria-controls="dynamic-table" tabindex="0"> ' +
        '<a href="#" onclick=loadUsers('+(currPage-1)+','+pageSize+','+parentId+') class="page-action" id="previous_button">前一页</a> </li> ' +
        '<li class="paginate_button active" aria-controls="dynamic-table" tabindex="0"> ' +
        '<a href="#" data-id="{{pageNo}}" >第'+currPage+'页</a> <input type="hidden" class="pageNo" value="{{pageNo}}" /> </li> ' +
        '<li class="paginate_button {{^nextUrl}}disabled{{/nextUrl}}" aria-controls="dynamic-table" tabindex="0"> ' +
        '<a href="#" onclick=loadUsers('+(currPage+1)+','+pageSize+','+parentId+') class="page-action" id="paginate_button">后一页</a> </li> ' +
        '<li class="paginate_button next {{^lastUrl}}disabled{{/lastUrl}}" aria-controls="dynamic-table" tabindex="0"> ' +
        '<a href="#" onclick=loadUsers('+pageCount+','+pageSize+','+parentId+') class="page-action">尾页</a> </li> ' +
        '</ul> </div> </div>';
}

function showAcls(totalCounts,from,to,currPage,pageSize,pageCount,parentId) {
    document.getElementById("turnPage").innerHTML = '<div class="col-xs-6"> ' +
        '<div class="dataTables_info" id="dynamic-table_info" role="status" aria-live="polite">总共 '+totalCounts+' 中的 '+from+' ~ '+to+' </div> </div> ' +
        '<div class="col-xs-6"> <div class="dataTables_paginate paging_simple_numbers" id="dynamic-table_paginate"> ' +
        '<ul class="pagination"> ' +
        '<li class="paginate_button previous {{^firstUrl}}disabled{{/firstUrl}}" aria-controls="dynamic-table" tabindex="0"> ' +
        '<a href="#" onclick=loadAcls(1,'+pageSize+','+parentId+') class="page-action">首页</a> </li> ' +
        '<li class="paginate_button {{^beforeUrl}}disabled{{/beforeUrl}}" aria-controls="dynamic-table" tabindex="0"> ' +
        '<a href="#" onclick=loadAcls('+(currPage-1)+','+pageSize+','+parentId+') class="page-action" id="previous_button">前一页</a> </li> ' +
        '<li class="paginate_button active" aria-controls="dynamic-table" tabindex="0"> ' +
        '<a href="#" data-id="{{pageNo}}" >第'+currPage+'页</a> <input type="hidden" class="pageNo" value="{{pageNo}}" /> </li> ' +
        '<li class="paginate_button {{^nextUrl}}disabled{{/nextUrl}}" aria-controls="dynamic-table" tabindex="0"> ' +
        '<a href="#" onclick=loadAcls('+(currPage+1)+','+pageSize+','+parentId+') class="page-action" id="paginate_button">后一页</a> </li> ' +
        '<li class="paginate_button next {{^lastUrl}}disabled{{/lastUrl}}" aria-controls="dynamic-table" tabindex="0"> ' +
        '<a href="#" onclick=loadAcls('+pageCount+','+pageSize+','+parentId+') class="page-action">尾页</a> </li> ' +
        '</ul> </div> </div>';
}

//设置上一页，下一页不可用
function setDisabled(currPage,pageCount){
    var previous=$("#previous_button");
    var paginate=$("#paginate_button");

    if(currPage<=1){
        previous.attr("disabled",true);
        previous.css("pointer-events","none");

        paginate.attr("disabled",false);
        paginate.css("pointer-events","");
    }
    if(currPage>=pageCount){
        previous.attr("disabled",false);
        previous.css("pointer-events","");

        paginate.attr("disabled",true);
        paginate.css("pointer-events","none");
    }
}