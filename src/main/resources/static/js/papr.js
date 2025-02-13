$(document).ready(function() {
	$('#addTime').prop('readonly', true);
	$('#addTime').click(function() {
					showDatePickerOnElement("addTime");
	            });
        });
		
		function showDatePickerOnElement(elementId) {
		    // 获取要应用日期选择器的元素
		    var element = document.getElementById(elementId);
		    
		    if (element) {
		        // 使用 WdatePicker 函数给元素添加日期选择器功能
		        // 这里传递一个配置对象，可以根据需要调整
		        WdatePicker({
		            el: elementId, // 这会直接绑定到ID为elementId的元素
		            dateFmt: 'yyyy-MM-dd', // 日期格式
		            minDate: '%y-%M-%d', // 最小日期（今天）
		            isShowClear: true, // 显示清空按钮
		            isShowToday: true, // 显示今天按钮
		            readOnly: true, // 设置输入框为只读
		            onpicking: function(dp) {
		                // 当选择日期时执行的函数
		                console.log('选中的日期是: ' + dp.cal.getNewDateStr());
		            }
		        });
		    } else {
		        console.error('找不到ID为' + elementId + '的元素');
		    }
		}
