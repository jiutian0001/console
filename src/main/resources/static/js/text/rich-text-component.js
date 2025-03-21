// richTextEditor.js
class RichTextEditor {
    constructor(targetElement, options = {}) {
        this.targetElement = typeof targetElement === 'string' ? 
            document.querySelector(targetElement) : targetElement;
        if (!this.targetElement) {
            console.error('Target element not found');
            return;
        }
        this.options = {
            height: options.height || '400px',
            width: options.width || '100%',
            toolbarItems: options.toolbarItems || ['style', 'text', 'align', 'list', 'image', 'code', 'table'],
            onChange: options.onChange || null
        };
        
        this.init();
    }

    init() {
        this.createWrapper();
        this.createToolbar();
        this.createEditors();
        this.bindEvents();
        this.syncContent();
    }

    createWrapper() {
        this.wrapper = document.createElement('div');
        this.wrapper.className = 'rich-editor-wrapper';
        this.wrapper.style.width = this.options.width;
        
        const style = document.createElement('style');
        style.textContent = `
            .rich-editor-wrapper {
                border: 1px solid #e0e0e0;
                border-radius: 8px;
                margin: 15px 0;
                background: #fff;
                box-shadow: 0 2px 5px rgba(0,0,0,0.05);
            }
            .rich-editor-toolbar {
                padding: 10px;
                border-bottom: 1px solid #e0e0e0;
                background: #fafafa;
                display: flex;
                flex-wrap: wrap;
                gap: 8px;
                border-radius: 8px 8px 0 0;
            }
            .rich-editor-toolbar .group {
                display: flex;
                gap: 4px;
                padding-right: 12px;
                margin-right: 12px;
                border-right: 1px solid #e0e0e0;
            }
            .rich-editor-btn {
                padding: 6px 12px;
                background: #fff;
                border: 1px solid #d0d0d0;
                border-radius: 6px;
                cursor: pointer;
                font-size: 14px;
                transition: all 0.2s;
            }
            .rich-editor-btn:hover {
                background: #f0f0f0;
                border-color: #b0b0b0;
            }
            .rich-editor-btn.active {
                background: #e6f0fa;
                border-color: #007bff;
            }
            .rich-editor-content {
                padding: 15px;
                min-height: ${this.options.height};
                outline: none;
                line-height: 1.5;
            }
            .rich-editor-html {
                width: 100%;
                min-height: ${this.options.height};
                padding: 15px;
                font-family: monospace;
                border: none;
                outline: none;
                resize: vertical;
                display: none;
            }
            .rich-editor-content img {
                max-width: 100%;
                height: auto;
                cursor: pointer;
                border-radius: 4px;
            }
            .rich-editor-content img.selected {
                border: 2px solid #007bff;
            }
            .rich-editor-content table {
                border-collapse: collapse;
                margin: 15px 0;
                background: #fff;
            }
            .rich-editor-content table, 
            .rich-editor-content th, 
            .rich-editor-content td {
                border: 1px solid #d0d0d0;
                padding: 10px;
                min-width: 120px;
                min-height: 40px;
            }
            .rich-editor-content th {
                background: #f5f5f5;
                font-weight: bold;
            }
            .table-dialog, .color-dialog, .width-dialog {
                position: fixed;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                background: #fff;
                padding: 20px;
                border: 1px solid #e0e0e0;
                border-radius: 8px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                z-index: 1000;
            }
            .table-dialog input, .width-dialog input {
                width: 60px;
                margin: 0 10px;
                padding: 6px;
                border: 1px solid #d0d0d0;
                border-radius: 4px;
            }
            .table-dialog button, .color-dialog button, .width-dialog button {
                padding: 6px 12px;
                margin: 0 5px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                background: #007bff;
                color: white;
                transition: background 0.2s;
            }
            .table-dialog button:hover, .color-dialog button:hover, .width-dialog button:hover {
                background: #0056b3;
            }
        `;
        document.head.appendChild(style);
        
        this.targetElement.parentNode.insertBefore(this.wrapper, this.targetElement.nextSibling);
        this.targetElement.style.display = 'none';
    }

    createToolbar() {
        const toolbar = document.createElement('div');
        toolbar.className = 'rich-editor-toolbar';

        if (this.options.toolbarItems.includes('style')) {
            const group = document.createElement('div');
            group.className = 'group';
            group.innerHTML = `
                <select class="rich-editor-btn" title="设置文本样式">
                    <option value="p">段落</option>
                    <option value="h1">标题1</option>
                    <option value="h2">标题2</option>
                    <option value="h3">标题3</option>
                    <option value="h4">标题4</option>
                </select>
            `;
            toolbar.appendChild(group);
        }

        if (this.options.toolbarItems.includes('text')) {
            const group = document.createElement('div');
            group.className = 'group';
            group.innerHTML = `
                <button type="button" class="rich-editor-btn" data-command="bold" title="加粗">B</button>
                <button type="button" class="rich-editor-btn" data-command="italic" title="斜体">I</button>
                <button type="button" class="rich-editor-btn" data-command="underline" title="下划线">U</button>
            `;
            toolbar.appendChild(group);
        }

        if (this.options.toolbarItems.includes('list')) {
            const group = document.createElement('div');
            group.className = 'group';
            group.innerHTML = `
                <button type="button" class="rich-editor-btn" data-command="insertUnorderedList" title="无序列表">•</button>
                <button type="button" class="rich-editor-btn" data-command="insertOrderedList" title="有序列表">1.</button>
            `;
            toolbar.appendChild(group);
        }

        if (this.options.toolbarItems.includes('align')) {
            const group = document.createElement('div');
            group.className = 'group';
            group.innerHTML = `
                <button type="button" class="rich-editor-btn" data-command="justifyLeft" title="左对齐">←</button>
                <button type="button" class="rich-editor-btn" data-command="justifyCenter" title="居中对齐">↔</button>
                <button type="button" class="rich-editor-btn" data-command="justifyRight" title="右对齐">→</button>
            `;
            toolbar.appendChild(group);
        }

        if (this.options.toolbarItems.includes('table')) {
            const group = document.createElement('div');
            group.className = 'group';
            group.innerHTML = `
                <button type="button" class="rich-editor-btn" id="tableBtn" title="插入表格">表格</button>
                <button type="button" class="rich-editor-btn" id="tableColorBtn" title="表格边框颜色">颜色</button>
                <button type="button" class="rich-editor-btn" id="tableWidthBtn" title="表格边框宽度">宽度</button>
            `;
            toolbar.appendChild(group);
        }

        if (this.options.toolbarItems.includes('image')) {
            const group = document.createElement('div');
            group.className = 'group';
            group.innerHTML = `
                <button type="button" class="rich-editor-btn" id="imageBtn" title="插入图片">图片</button>
                <input type="file" id="imageInput" accept="image/*" style="display: none;">
            `;
            toolbar.appendChild(group);
        }

        if (this.options.toolbarItems.includes('code')) {
            const group = document.createElement('div');
            group.className = 'group';
            group.innerHTML = `
                <button type="button" class="rich-editor-btn" id="codeToggle" title="切换代码视图">Code</button>
            `;
            toolbar.appendChild(group);
        }

        this.wrapper.appendChild(toolbar);
    }

    createEditors() {
        this.visualEditor = document.createElement('div');
        this.visualEditor.className = 'rich-editor-content';
        this.visualEditor.contentEditable = true;
        
        this.htmlEditor = document.createElement('textarea');
        this.htmlEditor.className = 'rich-editor-html';
        
        this.wrapper.appendChild(this.visualEditor);
        this.wrapper.appendChild(this.htmlEditor);
    }

    bindEvents() {
        this.wrapper.querySelectorAll('[data-command]').forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                document.execCommand(btn.dataset.command, false, null);
            });
        });

        const styleSelect = this.wrapper.querySelector('select');
        if (styleSelect) {
            styleSelect.addEventListener('change', (e) => {
                e.preventDefault();
                document.execCommand('formatBlock', false, e.target.value);
            });
        }

        const codeToggle = this.wrapper.querySelector('#codeToggle');
        if (codeToggle) {
            codeToggle.addEventListener('click', (e) => {
                e.preventDefault();
                this.toggleCodeView();
            });
        }

        const imageBtn = this.wrapper.querySelector('#imageBtn');
        const imageInput = this.wrapper.querySelector('#imageInput');
        if (imageBtn && imageInput) {
            imageBtn.addEventListener('click', (e) => {
                e.preventDefault();
                imageInput.click();
            });
            imageInput.addEventListener('change', (e) => this.handleImageUpload(e));
        }

        const tableBtn = this.wrapper.querySelector('#tableBtn');
        if (tableBtn) {
            tableBtn.addEventListener('click', (e) => {
                e.preventDefault();
                this.showTableDialog();
            });
        }

        const tableColorBtn = this.wrapper.querySelector('#tableColorBtn');
        if (tableColorBtn) {
            tableColorBtn.addEventListener('click', (e) => {
                e.preventDefault();
                this.showColorDialog();
            });
        }

        const tableWidthBtn = this.wrapper.querySelector('#tableWidthBtn');
        if (tableWidthBtn) {
            tableWidthBtn.addEventListener('click', (e) => {
                e.preventDefault();
                this.showWidthDialog();
            });
        }

        this.visualEditor.addEventListener('input', () => this.syncContent());
        this.htmlEditor.addEventListener('input', () => this.syncContent());

        this.visualEditor.addEventListener('keydown', (e) => {
            if (e.key === 'Tab') {
                e.preventDefault();
                if (e.shiftKey) {
                    document.execCommand('outdent', false, null);
                } else {
                    document.execCommand('insertText', false, '    ');
                }
            } else if (e.key === 'Enter') {
                e.stopPropagation();
            }
        });
    }

    showTableDialog() {
        const dialog = document.createElement('div');
        dialog.className = 'table-dialog';
        dialog.innerHTML = `
            <label>行数: <input type="number" id="tableRows" min="1" value="3"></label>
            <label>列数: <input type="number" id="tableCols" min="1" value="3"></label>
            <button id="confirmTable">确定</button>
            <button id="cancelTable">取消</button>
        `;
        document.body.appendChild(dialog);

        const confirmBtn = dialog.querySelector('#confirmTable');
        const cancelBtn = dialog.querySelector('#cancelTable');
        const rowsInput = dialog.querySelector('#tableRows');
        const colsInput = dialog.querySelector('#tableCols');

        confirmBtn.addEventListener('click', () => {
            const rows = parseInt(rowsInput.value) || 3;
            const cols = parseInt(colsInput.value) || 3;
            this.insertTable(rows, cols);
            document.body.removeChild(dialog);
        });

        cancelBtn.addEventListener('click', () => {
            document.body.removeChild(dialog);
        });
    }

    showColorDialog() {
        const dialog = document.createElement('div');
        dialog.className = 'color-dialog';
        dialog.innerHTML = `
            <label>选择边框颜色: <input type="color" id="tableColor" value="#000000"></label>
            <button id="confirmColor">确定</button>
            <button id="cancelColor">取消</button>
        `;
        document.body.appendChild(dialog);

        const confirmBtn = dialog.querySelector('#confirmColor');
        const cancelBtn = dialog.querySelector('#cancelColor');
        const colorInput = dialog.querySelector('#tableColor');

        confirmBtn.addEventListener('click', () => {
            this.setTableBorderColor(colorInput.value);
            document.body.removeChild(dialog);
        });

        cancelBtn.addEventListener('click', () => {
            document.body.removeChild(dialog);
        });
    }

    showWidthDialog() {
        const dialog = document.createElement('div');
        dialog.className = 'width-dialog';
        dialog.innerHTML = `
            <label>边框宽度 (1-10px): <input type="number" id="tableWidth" min="1" max="10" value="1"></label>
            <button id="confirmWidth">确定</button>
            <button id="cancelWidth">取消</button>
        `;
        document.body.appendChild(dialog);

        const confirmBtn = dialog.querySelector('#confirmWidth');
        const cancelBtn = dialog.querySelector('#cancelWidth');
        const widthInput = dialog.querySelector('#tableWidth');

        confirmBtn.addEventListener('click', () => {
            const width = parseInt(widthInput.value) || 1;
            this.setTableBorderWidth(width);
            document.body.removeChild(dialog);
        });

        cancelBtn.addEventListener('click', () => {
            document.body.removeChild(dialog);
        });
    }

    insertTable(rows, cols) {
        let tableHtml = '<table><tbody>';
        for (let i = 0; i < rows; i++) {
            tableHtml += '<tr>';
            for (let j = 0; j < cols; j++) {
                tableHtml += '<td></td>';
            }
            tableHtml += '</tr>';
        }
        tableHtml += '</tbody></table>';
        this.visualEditor.focus();
        document.execCommand('insertHTML', false, tableHtml);
    }

    setTableBorderColor(color) {
        const tables = this.visualEditor.querySelectorAll('table');
        tables.forEach(table => {
            table.style.borderColor = color;
            const cells = table.querySelectorAll('td, th');
            cells.forEach(cell => cell.style.borderColor = color);
        });
    }

    setTableBorderWidth(width) {
        const tables = this.visualEditor.querySelectorAll('table');
        tables.forEach(table => {
            table.style.borderWidth = `${width}px`;
            const cells = table.querySelectorAll('td, th');
            cells.forEach(cell => cell.style.borderWidth = `${width}px`);
        });
    }

    toggleCodeView() {
        const isHtmlMode = this.htmlEditor.style.display === 'block';
        if (isHtmlMode) {
            this.visualEditor.innerHTML = this.htmlEditor.value;
            this.visualEditor.style.display = 'block';
            this.htmlEditor.style.display = 'none';
        } else {
            this.htmlEditor.value = this.formatHtml(this.visualEditor.innerHTML);
            this.htmlEditor.style.display = 'block';
            this.visualEditor.style.display = 'none';
        }
    }

    handleImageUpload(e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                const img = document.createElement('img');
                img.src = e.target.result;
                this.visualEditor.focus();
                document.execCommand('insertHTML', false, img.outerHTML);
            };
            reader.readAsDataURL(file);
        }
    }

    syncContent() {
        const isHtmlMode = this.htmlEditor.style.display === 'block';
        const content = isHtmlMode ? this.htmlEditor.value : this.visualEditor.innerHTML;
        this.targetElement.value = content;
        
        if (this.options.onChange) {
            this.options.onChange(content);
        }
    }

    formatHtml(html) {
        let formatted = '';
        let indent = 0;
        const tab = '    ';
        html.split(/>\s*</).forEach(function(element) {
            if (element.match(/^\/\w/)) indent--;
            formatted += tab.repeat(indent) + '<' + element + '>\n';
            if (element.match(/^<?\w[^>]*[^\/]$/) && !element.startsWith("input")) indent++;
        });
        return formatted.substring(1, formatted.length-3);
    }

    getContent() {
        return this.targetElement.value;
    }

    setContent(html) {
        this.visualEditor.innerHTML = html;
        this.htmlEditor.value = this.formatHtml(html);
        this.syncContent();
    }

    destroy() {
        this.wrapper.remove();
        this.targetElement.style.display = '';
    }
}