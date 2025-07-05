$(document).ready(function() {
  // 根据 name 属性选择所有元素（例如 name="numDisplay"）
  $('[name="td-tv"]').each(function() {
    // 获取当前元素的文本并转换为数字
    var $element = $(this);
    var num = parseFloat($element.text());
    
    if (!isNaN(num)) { // 确保是有效数字
      var formatted;
      if (num >= 100000000) {
        // 上亿，单位为亿，保留两位小数并去除多余小数
        formatted = parseFloat((num / 100000000).toFixed(2)) + "亿";
      } else if (num >= 10000) {
        // 千万及以上，单位为万，保留两位小数并去除多余小数
        formatted = parseFloat((num / 10000).toFixed(2)) + "万";
      } else {
        // 小于万，直接显示
        formatted = num;
      }
      
      // 回显到当前元素
      $element.text(formatted);
      
    }
  });
  
  
  var $trs = $("tr:has(td[name='td-ma'])");
       
       // 遍历 <tr>，按页面顺序计算增量
       $trs.each(function(index) {
         var $td = $(this).find("td[name='td-ma']");
         var $element = $td.find("div"); // 优先找 <div>
         if ($element.length === 0) {
           $element = $td; // 如果没有 <div>，用 <td>
         }
         
         var num = parseFloat($element.text());
         if (!isNaN(num)) {
           // 保留源数据
           var formatted = num.toString();
           
           // 计算增量（除最后一行）
           var increment = "";
           if (index < $trs.length - 1) { // 非最后一行（最早一天）
             var nextTd = $trs.eq(index + 1).find("td[name='td-ma']");
             var nextElement = nextTd.find("div");
             if (nextElement.length === 0) {
               nextElement = nextTd;
             }
             var nextNum = parseFloat(nextElement.text());
             if (!isNaN(nextNum)) {
               var diff = num - nextNum;
               increment = diff >= 0 ? "+" + diff : diff.toString(); // 正增量带+，负增量无+
               increment = '<span style="color: green;">' + increment + '</span>'; // 绿色显示
             }
           }
           
           // 回显 源数据+增量
           $element.html(formatted + increment);
         }
       });
});
   
