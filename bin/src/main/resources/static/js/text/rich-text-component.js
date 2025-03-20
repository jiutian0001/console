// richTextEditor.js
class RichTextEditor {
    constructor(targetElement, options = {}) {
        this.targetElement = typeof targetElement === 'string' ? 
            document.querySelector(targetElement) : targetElement;
        this.options = {
            height: options.height || '400px',
            width: options.width || '100%',
            toolbarItems: options.toolbarItems || ['style', 'text', 'align', 'list', 'image', 'code'],
            onChange: options.onChange || null
        };
        
        this.init();
    }

    init() {
        // 创建编辑器容器
        this.createWrapper();
        // 初始化工具栏
        this.createToolbar();
        // 创建编辑区域
        this.createEditors();
        // 绑定事件
        this.bindEvents();
        // 同步初始内容
        this.syncContent();
    }

    createWrapper() {
        this.wrapper = document.createElement('div');
        this.wrapper.className = 'rich-editor-wrapper';
        this.wrapper.style.width = this.options.width;
        
        // 插入样式
        const style = document.createElement('style');
        style.textContent = `
            .rich-editor-wrapper {
                border: 1px solid #ddd;
                border-radius: 4px;
                margin: 10px 0;
                background: #fff;
            }
            .rich-editor-toolbar {
                padding: 10px;
                border-bottom: 1px solid #ddd;
                background: #f5f5f5;
                display: flex;
                flex-wrap: wrap;
                gap: 5px;
            }
            .rich-editor-toolbar .group {
                display: flex;
                gap: 2px;
                padding-right: 10px;
                margin-right: 10px;
                border-right: 1px solid #ddd;
            }
            .rich-editor-btn {
                padding: 6px 12px;
                background: white;
                border: 1px solid #ddd;
                border-radius: 4px;
                cursor: pointer;
                font-size: 14px;
            }
            .rich-editor-btn:hover {
                background: #e9ecef;
            }
            .rich-editor-btn.active {
                background: #e9ecef;
                border-color: #0056b3;
            }
            .rich-editor-content {
                padding: 10px;
                min-height: ${this.options.height};
                outline: none;
            }
            .rich-editor-html {
                width: 100%;
                min-height: ${this.options.height};
                padding: 10px;
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
            }
            .rich-editor-content img.selected {
                border: 2px solid #0056b3;
            }
        `;
        document.head.appendChild(style);
        
        // 插入编辑器到目标元素之后
        this.targetElement.parentNode.insertBefore(this.wrapper, this.targetElement.nextSibling);
        // 隐藏原始元素
        this.targetElement.style.display = 'none';
    }

    createToolbar() {
        const toolbar = document.createElement('div');
        toolbar.className = 'rich-editor-toolbar';

        if (this.options.toolbarItems.includes('style')) {
            const group = document.createElement('div');
            group.className = 'group';
            group.innerHTML = `
                <select class="rich-editor-btn">
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
                <button type="button" class="rich-editor-btn" data-command="bold">B</button>
                <button type="button" class="rich-editor-btn" data-command="italic">I</button>
                <button type="button" class="rich-editor-btn" data-command="underline">U</button>
            `;
            toolbar.appendChild(group);
        }

        if (this.options.toolbarItems.includes('list')) {
            const group = document.createElement('div');
            group.className = 'group';
            group.innerHTML = `
                <button type="button" class="rich-editor-btn" data-command="insertUnorderedList">•</button>
                <button type="button" class="rich-editor-btn" data-command="insertOrderedList">1.</button>
            `;
            toolbar.appendChild(group);
        }

        if (this.options.toolbarItems.includes('align')) {
            const group = document.createElement('div');
            group.className = 'group';
            group.innerHTML = `
                <button type="button" class="rich-editor-btn" data-command="justifyLeft">←</button>
                <button type="button" class="rich-editor-btn" data-command="justifyCenter">↔</button>
                <button type="button" class="rich-editor-btn" data-command="justifyRight">→</button>
            `;
            toolbar.appendChild(group);
        }

        if (this.options.toolbarItems.includes('image')) {
            const group = document.createElement('div');
            group.className = 'group';
            group.innerHTML = `
                <button type="button" class="rich-editor-btn" id="imageBtn">图片</button>
                <input type="file" id="imageInput" accept="image/*" style="display: none;">
            `;
            toolbar.appendChild(group);
        }

        if (this.options.toolbarItems.includes('code')) {
            const group = document.createElement('div');
            group.className = 'group';
            group.innerHTML = `
                <button type="button" class="rich-editor-btn" id="codeToggle">Code</button>
            `;
            toolbar.appendChild(group);
        }

        this.wrapper.appendChild(toolbar);
    }

    createEditors() {
        // 可视化编辑器
        this.visualEditor = document.createElement('div');
        this.visualEditor.className = 'rich-editor-content';
        this.visualEditor.contentEditable = true;
        
        // HTML编辑器
        this.htmlEditor = document.createElement('textarea');
        this.htmlEditor.className = 'rich-editor-html';
        
        this.wrapper.appendChild(this.visualEditor);
        this.wrapper.appendChild(this.htmlEditor);
    }

    bindEvents() {
        // 命令按钮事件
        this.wrapper.querySelectorAll('[data-command]').forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                document.execCommand(btn.dataset.command, false, null);
            });
        });

        // 标题选择事件
        const styleSelect = this.wrapper.querySelector('select');
        if (styleSelect) {
            styleSelect.addEventListener('change', (e) => {
                e.preventDefault();
                document.execCommand('formatBlock', false, e.target.value);
            });
        }

        // 代码切换事件
        const codeToggle = this.wrapper.querySelector('#codeToggle');
        if (codeToggle) {
            codeToggle.addEventListener('click', (e) => {
                e.preventDefault();
                this.toggleCodeView();
            });
        }

        // 图片上传事件
        const imageBtn = this.wrapper.querySelector('#imageBtn');
        const imageInput = this.wrapper.querySelector('#imageInput');
        if (imageBtn && imageInput) {
            imageBtn.addEventListener('click', (e) => {
                e.preventDefault();
                imageInput.click();
            });
            imageInput.addEventListener('change', (e) => this.handleImageUpload(e));
        }

        // 内容同步事件
        this.visualEditor.addEventListener('input', () => this.syncContent());
        this.htmlEditor.addEventListener('input', () => this.syncContent());
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

    // 公共方法
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