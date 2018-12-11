<script id="paginateTemplate" type="x-tmpl-mustache">
<div class="col-xs-6">
    <div class="dataTables_info" id="dynamic-table_info" role="status" aria-live="polite">
        总共 {{total}} 中的 {{from}} ~ {{to}}
    </div>
</div>

<div class="col-xs-6">
    <div class="dataTables_paginate paging_simple_numbers" id="dynamic-table_paginate">
        <ul class="pagination">
            <li class="paginate_button previous {{^firstUrl}}disabled{{/firstUrl}}" aria-controls="dynamic-table" tabindex="0">
                <a href="#" data-target="1" data-url="{{firstUrl}}" class="page-action">首页</a>
            </li>
            <li class="paginate_button {{^beforeUrl}}disabled{{/beforeUrl}}" aria-controls="dynamic-table" tabindex="0">
                <a href="#" data-target="{{beforePageNo}}" data-url="{{beforeUrl}}" class="page-action">前一页</a>
            </li>
            <li class="paginate_button active" aria-controls="dynamic-table" tabindex="0">
                <a href="#" data-id="{{pageNo}}" >第{{pageNo}}页</a>
                <input type="hidden" class="pageNo" value="{{pageNo}}" />
            </li>
            <li class="paginate_button {{^nextUrl}}disabled{{/nextUrl}}" aria-controls="dynamic-table" tabindex="0">
                <a href="#" data-target="{{nextPageNo}}" data-url="{{nextUrl}}" class="page-action">后一页</a>
            </li>
            <li class="paginate_button next {{^lastUrl}}disabled{{/lastUrl}}" aria-controls="dynamic-table" tabindex="0">
                <a href="#" data-target="{{maxPageNo}}" data-url="{{lastUrl}}" class="page-action">尾页</a>
            </li>
        </ul>
    </div>
</div>
</script>

<script type="text/javascript">
    var paginateTemplate = $("#paginateTemplate").html();
    Mustache.parse(paginateTemplate);

    function renderPage(url, total, currPage, pageSize, currentSize, idElement, callback,type,operator) {

        currPage=parseInt(currPage);
        pageSize=parseInt(pageSize);
        var maxPageNo = Math.ceil(total / pageSize);
        var paramStartChar = url.indexOf("?") > 0 ? "&" : "?";
        var from = (currPage - 1) * pageSize + 1;
        var view = {
            from: from > total ? total : from,
            to: (from + currentSize - 1) > total ? total : (from + currentSize - 1),
            total : total,
            currPage : currPage,
            maxPageNo : maxPageNo,
            nextPageNo: currPage >= maxPageNo ? maxPageNo : (currPage + 1),
            beforePageNo : currPage == 1 ? 1 : (currPage - 1),
            firstUrl : (currPage == 1) ? '' : (url + paramStartChar + "currPage=1&pageSize=" + pageSize+"&type="+type+"&operator="+operator),
            beforeUrl: (currPage == 1) ? '' : (url + paramStartChar + "currPage=" + (currPage - 1) + "&pageSize=" + pageSize+"&type="+type+"&operator="+operator),
            nextUrl : (currPage >= maxPageNo) ? '' : (url + paramStartChar + "currPage=" + (currPage + 1) + "&pageSize=" + pageSize+"&type="+type+"&operator="+operator),
            lastUrl : (currPage >= maxPageNo) ? '' : (url + paramStartChar + "currPage=" + maxPageNo + "&pageSize=" + pageSize+"&type="+type+"&operator="+operator)
        };
        $("#" + idElement).html(Mustache.render(paginateTemplate, view));

        $(".page-action").click(function(e) {
            e.preventDefault();
            $("#" + idElement + " .pageNo").val($(this).attr("data-target"));
            var targetUrl  = $(this).attr("data-url");
            if(targetUrl != '') {
                $.ajax({
                    url : targetUrl,
                    success: function (result) {
                        if (callback) {
                            callback(result, url);
                        }
                    }
                })
            }
        })
    }
</script>
