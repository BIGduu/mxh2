<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Normal Modal -->
<div class="modal" id="modal-common" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="block block-themed block-transparent remove-margin-b">
                <div class="block-header bg-primary-dark">
                    <ul class="block-options dialog-block-options">
                        <li>
                            <button data-dismiss="modal" type="button" id="modal-common-close"><i class="si si-close"></i></button>
                        </li>
                    </ul>
                    <h3 class="block-title dialog-block-title" id="modal-common-title"></h3>
                </div>
                <div class="block-content modal-common-block-content" id="modal-common-content">
                	
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-sm btn-default" type="button" id="modal-common-cancel" data-dismiss="modal">取消</button>
                <button class="btn btn-sm btn-primary" type="button" id="modal-common-confirm"><i class="fa fa-check"></i> 确认</button>
            </div>
        </div>
    </div>
</div>
<!-- END Normal Modal -->

<!-- Large Modal -->
<div class="modal" id="modal-large" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-lg" style="margin-top: 100px;">
		<div class="modal-content">
			<div class="block block-themed block-transparent remove-margin-b">
				<div class="block-header bg-primary-dark">
					<ul class="block-options dialog-block-options">
						<li>
							<button data-dismiss="modal" type="button" id="modal-large-close"><i class="si si-close"></i></button>
						</li>
					</ul>
					<h3 class="block-title dialog-block-title" id="modal-large-title"></h3>
				</div>
				<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successDialogTips"></div>
				<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failDialogTips"></div>
				<div class="block-content modal-large-block-content" id="modal-large-content"></div>
			</div>
			<div class="modal-footer" style="border-top: none;">
				<button class="btn btn-sm btn-default" type="button" id="modal-large-cancel" data-dismiss="modal">取消</button>
				<button class="btn btn-sm btn-primary" type="button" data-dismiss="modal" id="modal-large-confirm"><i class="fa fa-check"></i> 确认</button>
			</div>
		</div>
	</div>
</div>
<!-- END Large Modal -->