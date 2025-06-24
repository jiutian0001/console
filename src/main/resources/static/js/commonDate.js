$(document).ready(function() {
           // 获取隐藏输入框中的 ID 字符串
           var elementIds = $("#date_ids").val(); // 例如 "dateTime1-d,dateTime2-m,dateTime3,dateTime4-s"
           if (elementIds) {
               // 按逗号分割成数组
               var idsArray = elementIds.split(',');

               // 循环为每个 ID 绑定日期选择器
               idsArray.forEach(function(item) {
                   if (item.trim()) {
                       // 分割 ID 和后缀
                       var parts = item.trim().split('-');
                       var id = parts[0]; // 提取 ID
                       var suffix = parts[1] || ''; // 提取后缀（d, m, s 或空）
                       // 设置只读属性并绑定点击事件
                       $('#' + id).prop('readonly', true);
                       $('#' + id).click(function() {
                           // 根据后缀设置日期格式
                           var dateFormat;
                           switch (suffix) {
                               case 'm':
                                   dateFormat = 'yyyy-MM-dd HH:mm';
                                   break;
                               case 's':
                                   dateFormat = 'yyyy-MM-dd HH:mm:ss';
                                   break;
                               case 'd':
                               default:
                                   dateFormat = 'yyyy-MM-dd'; // 默认格式
                                   break;
                           }
                           showDatePickerOnElement(id, dateFormat);
                       });
                   }
               });
           }
       });
	
	function showDatePickerOnElement(elementId,dateFmt) {
	    // 获取要应用日期选择器的元素
	    var element = document.getElementById(elementId);
	    if (element) {
	        // 使用 WdatePicker 函数给元素添加日期选择器功能
	        // 这里传递一个配置对象，可以根据需要调整
	        WdatePicker({
	            el: elementId, // 这会直接绑定到ID为elementId的元素
	            dateFmt: dateFmt, // 日期格式
	           // minDate: '%y-%M-%d', // 最小日期（今天）
	            isShowClear: true, // 显示清空按钮
	            isShowToday: true, // 显示今天按钮
	            readOnly: true, // 设置输入框为只读
	            onpicking: function(dp) {
	                // 当选择日期时执行的函数
	                //console.log('选中的日期是: ' + dp.cal.getNewDateStr());
	            }
	        });
	    } else {
	        console.error('找不到ID为' + elementId + '的元素');
	    }
	}
