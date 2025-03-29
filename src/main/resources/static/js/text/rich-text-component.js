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
        this.isFullPage = false;
        
        this.init();
    }

    init() {
        this.createWrapper();
        this.createToolbar();
        this.createEditors();
        this.bindEvents();
        this.syncContent();
        if (!this.visualEditor.innerHTML.trim()) {
            this.visualEditor.innerHTML = '<p> </p>';
        }
    }

	createWrapper() {
	    this.wrapper = document.createElement('div');
	    this.wrapper.className = 'rich-editor-wrapper';
	    this.wrapper.style.width = this.options.width;
	    this.wrapper.style.maxHeight = this.options.height;
	    this.wrapper.style.overflowY = 'auto';
	    
	    const style = document.createElement('style');
	    style.textContent = `
	        .rich-editor-wrapper {
	            border: 1px solid #e0e0e0;
	            border-radius: 8px;
	            margin: 15px 0;
	            background: #fff;
	            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
	            position: relative;
	        }
	        .rich-editor-fullpage-container {
	            position: fixed;
	            top: 0;
	            left: 0;
	            width: 100vw;
	            height: 100vh;
	            background: #fff;
	            z-index: 9999;
	            display: flex;
	            flex-direction: column;
	        }
	        .rich-editor-toolbar {
	            padding: 10px;
	            border-bottom: 1px solid #e0e0e0;
	            background: #fafafa;
	            display: flex;
	            flex-wrap: wrap;
	            gap: 8px;
	            border-radius: 8px 8px 0 0;
	            position: sticky;
	            top: 0;
	            z-index: 10;
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
	            font-size: 16px; /* 默认字体大小 */
	        }
	        .rich-editor-fullpage-container .rich-editor-content {
	            flex: 1;
	            height: auto;
	            overflow-y: auto;
	            padding: 15px;
	            font-size: 16px; /* 默认字体大小 */
	        }
	        .rich-editor-content p {
	            margin: 0 0 10px 0;
	            padding: 0;
	        }
	        .rich-editor-content div {
	            margin: 0 0 10px 0;
	            padding: 0;
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
	            margin: 15px 0 15px 20px;
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
	            z-index: 10000;
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
	    
	    this.originalParent = this.targetElement.parentNode;
	    this.originalSibling = this.targetElement.nextSibling;
	    this.originalParent.insertBefore(this.wrapper, this.originalSibling);
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

	    // 新增字体大小调整选项
	    const fontSizeGroup = document.createElement('div');
	    fontSizeGroup.className = 'group';
	    fontSizeGroup.innerHTML = `
	        <select class="rich-editor-btn" id="fontSizeSelect" title="设置字体大小">
	            <option value="12px">12px</option>
	            <option value="14px">14px</option>
	            <option value="16px" selected>16px</option>
	            <option value="18px">18px</option>
	            <option value="20px">20px</option>
	            <option value="24px">24px</option>
	            <option value="30px">30px</option>
	            <option value="36px">36px</option>
	        </select>
	    `;
	    toolbar.appendChild(fontSizeGroup);

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

	    const fullPageGroup = document.createElement('div');
	    fullPageGroup.className = 'group';
	    fullPageGroup.innerHTML = `
	        <button type="button" class="rich-editor-btn" id="fullPageToggle" title="全页面编辑">全屏</button>
	    `;
	    toolbar.appendChild(fullPageGroup);

	    this.wrapper.appendChild(toolbar);
	    this.toolbar = toolbar;
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

	    const styleSelect = this.wrapper.querySelector('select[title="设置文本样式"]');
	    if (styleSelect) {
	        styleSelect.addEventListener('change', (e) => {
	            e.preventDefault();
	            document.execCommand('formatBlock', false, e.target.value);
	        });
	    }

	    const fontSizeSelect = this.wrapper.querySelector('#fontSizeSelect');
	    if (fontSizeSelect) {
	        fontSizeSelect.addEventListener('change', (e) => {
	            e.preventDefault();
	            const size = e.target.value;
	            const selection = window.getSelection();
	            if (selection.rangeCount > 0) {
	                const range = selection.getRangeAt(0);
	                let container = range.commonAncestorContainer;
	                if (container.nodeType === 3) container = container.parentElement;
	                if (container.nodeName === 'SPAN' && container.style.fontSize) {
	                    container.style.fontSize = size;
	                } else {
	                    const fragment = range.extractContents();
	                    const spans = fragment.querySelectorAll('span');
	                    spans.forEach(span => {
	                        if (span.style.fontSize) {
	                            const text = span.textContent;
	                            span.parentNode.replaceChild(document.createTextNode(text), span);
	                        }
	                    });
	                    const newSpan = document.createElement('span');
	                    newSpan.style.fontSize = size;
	                    newSpan.appendChild(fragment);
	                    range.insertNode(newSpan);
	                }
	                selection.removeAllRanges();
	                selection.addRange(range);
	            }
	            this.syncContent();
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

	    const fullPageToggle = this.wrapper.querySelector('#fullPageToggle');
	    if (fullPageToggle) {
	        fullPageToggle.addEventListener('click', (e) => {
	            e.preventDefault();
	            this.toggleFullPage();
	        });
	    }

	    this.visualEditor.addEventListener('input', () => this.syncContent());
	    this.htmlEditor.addEventListener('input', () => this.syncContent());

	    this.visualEditor.addEventListener('paste', (e) => {
	        e.preventDefault();
	        const clipboardData = e.clipboardData || window.clipboardData;
	        let pastedData = clipboardData.getData('text/html') || clipboardData.getData('text/plain');

	        if (!pastedData.includes('<')) {
	            pastedData = pastedData
	                .split('\n')
	                .filter(line => line.trim())
	                .map(line => `<p>${line}</p>`)
	                .join('');
	        } else {
	            pastedData = pastedData
	                .replace(/\r\n|\n/g, '</p><p>')
	                .replace(/<\/p><p>\s*<\/p><p>/g, '</p><p>');
	        }

	        document.execCommand('insertHTML', false, pastedData);
	        this.syncContent();
	    });

	    let lastEnterTime = 0;
	    this.visualEditor.addEventListener('keydown', (e) => {
	        if (e.key === 'Enter') {
	            e.preventDefault();
	            const selection = window.getSelection();
	            if (selection.rangeCount === 0) return;

	            const range = selection.getRangeAt(0);
	            let currentBlock = range.startContainer.nodeType === 1 
	                ? range.startContainer 
	                : range.startContainer.parentElement;

	            // 确保 currentBlock 是块级元素
	            while (currentBlock && !['P', 'LI', 'DIV'].includes(currentBlock.nodeName) && currentBlock !== this.visualEditor) {
	                currentBlock = currentBlock.parentElement;
	            }
	            if (!currentBlock) currentBlock = this.visualEditor;

	            const now = Date.now();
	            const isDoubleEnter = now - lastEnterTime < 500;
	            lastEnterTime = now;

	            let newNode;
	            const li = currentBlock.closest('li');
	            if (li) {
	                const parentUlOrOl = li.parentElement;
	                const isNested = parentUlOrOl.parentElement.tagName === 'LI';

	                if (isDoubleEnter) {
	                    if (isNested) {
	                        const parentLi = parentUlOrOl.parentElement;
	                        newNode = document.createElement('li');
	                        newNode.innerHTML = '<br>'; // 使用 <br> 确保空行可见
	                        parentLi.insertAdjacentElement('afterend', newNode);
	                    } else {
	                        newNode = document.createElement('p');
	                        newNode.innerHTML = '<br>';
	                        parentUlOrOl.insertAdjacentElement('afterend', newNode);
	                    }
	                } else {
	                    newNode = document.createElement('li');
	                    newNode.innerHTML = '<br>';
	                    li.insertAdjacentElement('afterend', newNode);
	                }
	            } else {
	                // 处理普通段落换行
	                newNode = document.createElement('p');
	                if (currentBlock === this.visualEditor || currentBlock.nodeName === 'DIV') {
	                    // 直接在编辑器根部或 DIV 中
	                    newNode.innerHTML = '<br>';
	                    this.visualEditor.appendChild(newNode);
	                } else {
	                    // 分割当前段落
	                    const fragment = range.extractContents();
	                    newNode.appendChild(fragment);
	                    if (!newNode.innerHTML.trim()) newNode.innerHTML = '<br>';
	                    currentBlock.insertAdjacentElement('afterend', newNode);

	                    // 确保当前块不为空
	                    if (!currentBlock.innerHTML.trim()) currentBlock.innerHTML = '<br>';
	                }
	            }

	            // 设置光标位置到新节点
	            const rangeNew = document.createRange();
	            rangeNew.selectNodeContents(newNode);
	            rangeNew.collapse(true); // 光标移到新节点开头
	            selection.removeAllRanges();
	            selection.addRange(rangeNew);

	            // 滚动到新节点位置
	            const rect = newNode.getBoundingClientRect();
	            const editorRect = this.visualEditor.getBoundingClientRect();
	            const scrollTop = this.visualEditor.scrollTop;
	            const relativeTop = rect.top - editorRect.top + scrollTop;

	            if (rect.bottom > editorRect.bottom) {
	                this.visualEditor.scrollTop = relativeTop - editorRect.height + rect.height + 10;
	            } else if (rect.top < editorRect.top) {
	                this.visualEditor.scrollTop = relativeTop - 10;
	            }

	            this.visualEditor.focus();
	            this.syncContent();
	        } else if (e.key === 'Tab') {
	            e.preventDefault();
	            const selection = window.getSelection();
	            if (selection.rangeCount > 0) {
	                const range = selection.getRangeAt(0);
	                const currentBlock = range.startContainer.nodeType === 1 
	                    ? range.startContainer 
	                    : range.startContainer.parentElement;

	                const li = currentBlock.closest('li');
	                if (li) {
	                    const parentUlOrOl = li.parentElement;
	                    const isNested = parentUlOrOl.parentElement.tagName === 'LI';

	                    if (!isNested && li.previousElementSibling) {
	                        const subList = document.createElement(parentUlOrOl.tagName);
	                        subList.appendChild(li.cloneNode(true));
	                        li.previousElementSibling.appendChild(subList);
	                        li.remove();
	                        const newLi = subList.querySelector('li');
	                        range.setStart(newLi, 0);
	                        range.setEnd(newLi, 0);
	                        selection.removeAllRanges();
	                        selection.addRange(range);
	                    } else {
	                        document.execCommand('indent', false, null);
	                    }
	                } else {
	                    document.execCommand('insertText', false, '    ');
	                }
	            }
	            this.syncContent();
	        }
	    });
	}
	
    toggleFullPage() {
        if (!this.isFullPage) {
            // 创建全屏容器
            this.fullPageContainer = document.createElement('div');
            this.fullPageContainer.className = 'rich-editor-fullpage-container';
            this.fullPageContainer.appendChild(this.toolbar.cloneNode(true)); // 复制工具栏
            this.fullPageContainer.appendChild(this.visualEditor.cloneNode(true)); // 复制编辑区域
            document.body.appendChild(this.fullPageContainer);

            // 绑定新工具栏的事件
            this.bindFullPageEvents();

            // 隐藏原始 wrapper
            this.wrapper.style.display = 'none';
            this.htmlEditor.style.display = 'none';
            this.isFullPage = true;

            // 更新全屏按钮文本
            this.fullPageContainer.querySelector('#fullPageToggle').textContent = '返回';
        } else {
            // 同步内容回原始编辑器
            const fullPageContent = this.fullPageContainer.querySelector('.rich-editor-content').innerHTML;
            this.visualEditor.innerHTML = fullPageContent;

            // 移除全屏容器
            document.body.removeChild(this.fullPageContainer);
            this.fullPageContainer = null;

            // 显示原始 wrapper
            this.wrapper.style.display = 'block';
            this.isFullPage = false;
        }
        this.syncContent();
        if (this.isFullPage) {
            this.fullPageContainer.querySelector('.rich-editor-content').focus();
        } else {
            this.visualEditor.focus();
        }
    }
	
	bindFullPageEvents() {
	    const fullPageToolbar = this.fullPageContainer.querySelector('.rich-editor-toolbar');
	    const fullPageEditor = this.fullPageContainer.querySelector('.rich-editor-content');

	    // 工具栏按钮事件绑定
	    fullPageToolbar.querySelectorAll('[data-command]').forEach(btn => {
	        btn.addEventListener('click', (e) => {
	            e.preventDefault();
	            document.execCommand(btn.dataset.command, false, null);
	        });
	    });

	    const styleSelect = fullPageToolbar.querySelector('select[title="设置文本样式"]');
	    if (styleSelect) {
	        styleSelect.addEventListener('change', (e) => {
	            e.preventDefault();
	            document.execCommand('formatBlock', false, e.target.value);
	        });
	    }

	    const fontSizeSelect = fullPageToolbar.querySelector('#fontSizeSelect');
	    if (fontSizeSelect) {
	        fontSizeSelect.addEventListener('change', (e) => {
	            e.preventDefault();
	            const size = e.target.value;
	            const selection = window.getSelection();
	            if (selection.rangeCount > 0) {
	                const range = selection.getRangeAt(0);
	                let container = range.commonAncestorContainer;
	                if (container.nodeType === 3) container = container.parentElement;
	                if (container.nodeName === 'SPAN' && container.style.fontSize) {
	                    container.style.fontSize = size;
	                } else {
	                    const fragment = range.extractContents();
	                    const spans = fragment.querySelectorAll('span');
	                    spans.forEach(span => {
	                        if (span.style.fontSize) {
	                            const text = span.textContent;
	                            span.parentNode.replaceChild(document.createTextNode(text), span);
	                        }
	                    });
	                    const newSpan = document.createElement('span');
	                    newSpan.style.fontSize = size;
	                    newSpan.appendChild(fragment);
	                    range.insertNode(newSpan);
	                }
	                selection.removeAllRanges();
	                selection.addRange(range);
	            }
	            this.syncContent();
	        });
	    }

	    const codeToggle = fullPageToolbar.querySelector('#codeToggle');
	    if (codeToggle) {
	        codeToggle.addEventListener('click', (e) => {
	            e.preventDefault();
	            alert('代码视图在全屏模式下暂不可用，请返回普通模式');
	        });
	    }

	    const imageBtn = fullPageToolbar.querySelector('#imageBtn');
	    const imageInput = fullPageToolbar.querySelector('#imageInput');
	    if (imageBtn && imageInput) {
	        imageBtn.addEventListener('click', (e) => {
	            e.preventDefault();
	            imageInput.click();
	        });
	        imageInput.addEventListener('change', (e) => {
	            const file = e.target.files[0];
	            if (file) {
	                const reader = new FileReader();
	                reader.onload = (ev) => {
	                    const img = document.createElement('img');
	                    img.src = ev.target.result;
	                    fullPageEditor.appendChild(img);
	                    this.syncContent();
	                };
	                reader.readAsDataURL(file);
	            }
	        });
	    }

	    const tableBtn = fullPageToolbar.querySelector('#tableBtn');
	    if (tableBtn) {
	        tableBtn.addEventListener('click', (e) => {
	            e.preventDefault();
	            this.showTableDialog();
	        });
	    }

	    const tableColorBtn = fullPageToolbar.querySelector('#tableColorBtn');
	    if (tableColorBtn) {
	        tableColorBtn.addEventListener('click', (e) => {
	            e.preventDefault();
	            this.showColorDialog();
	        });
	    }

	    const tableWidthBtn = fullPageToolbar.querySelector('#tableWidthBtn');
	    if (tableWidthBtn) {
	        tableWidthBtn.addEventListener('click', (e) => {
	            e.preventDefault();
	            this.showWidthDialog();
	        });
	    }

	    const fullPageToggle = fullPageToolbar.querySelector('#fullPageToggle');
	    if (fullPageToggle) {
	        fullPageToggle.addEventListener('click', (e) => {
	            e.preventDefault();
	            this.toggleFullPage();
	        });
	    }

	    fullPageEditor.addEventListener('input', () => this.syncContent());

	    fullPageEditor.addEventListener('paste', (e) => {
	        e.preventDefault();
	        const clipboardData = e.clipboardData || window.clipboardData;
	        let pastedData = clipboardData.getData('text/html') || clipboardData.getData('text/plain');

	        if (!pastedData.includes('<')) {
	            pastedData = pastedData
	                .split('\n')
	                .filter(line => line.trim())
	                .map(line => `<p>${line}</p>`)
	                .join('');
	        } else {
	            pastedData = pastedData
	                .replace(/\r\n|\n/g, '</p><p>')
	                .replace(/<\/p><p>\s*<\/p><p>/g, '</p><p>');
	        }

	        document.execCommand('insertHTML', false, pastedData);
	        this.syncContent();
	    });

	    let lastEnterTime = 0;
	    fullPageEditor.addEventListener('keydown', (e) => {
	        if (e.key === 'Enter') {
	            e.preventDefault();
	            const selection = window.getSelection();
	            if (selection.rangeCount === 0) return;

	            const range = selection.getRangeAt(0);
	            let currentBlock = range.startContainer.nodeType === 1 
	                ? range.startContainer 
	                : range.startContainer.parentElement;

	            // 确保 currentBlock 是块级元素
	            while (currentBlock && !['P', 'LI', 'DIV'].includes(currentBlock.nodeName) && currentBlock !== fullPageEditor) {
	                currentBlock = currentBlock.parentElement;
	            }
	            if (!currentBlock) currentBlock = fullPageEditor;

	            const now = Date.now();
	            const isDoubleEnter = now - lastEnterTime < 500;
	            lastEnterTime = now;

	            let newNode;
	            const li = currentBlock.closest('li');
	            if (li) {
	                const parentUlOrOl = li.parentElement;
	                const isNested = parentUlOrOl.parentElement.tagName === 'LI';

	                if (isDoubleEnter) {
	                    if (isNested) {
	                        const parentLi = parentUlOrOl.parentElement;
	                        newNode = document.createElement('li');
	                        newNode.innerHTML = '<br>';
	                        parentLi.insertAdjacentElement('afterend', newNode);
	                    } else {
	                        newNode = document.createElement('p');
	                        newNode.innerHTML = '<br>';
	                        parentUlOrOl.insertAdjacentElement('afterend', newNode);
	                    }
	                } else {
	                    newNode = document.createElement('li');
	                    newNode.innerHTML = '<br>';
	                    li.insertAdjacentElement('afterend', newNode);
	                }
	            } else {
	                // 处理普通段落换行
	                newNode = document.createElement('p');
	                if (currentBlock === fullPageEditor || currentBlock.nodeName === 'DIV') {
	                    newNode.innerHTML = '<br>';
	                    fullPageEditor.appendChild(newNode);
	                } else {
	                    const fragment = range.extractContents();
	                    newNode.appendChild(fragment);
	                    if (!newNode.innerHTML.trim()) newNode.innerHTML = '<br>';
	                    currentBlock.insertAdjacentElement('afterend', newNode);

	                    if (!currentBlock.innerHTML.trim()) currentBlock.innerHTML = '<br>';
	                }
	            }

	            // 设置光标位置到新节点
	            const rangeNew = document.createRange();
	            rangeNew.selectNodeContents(newNode);
	            rangeNew.collapse(true); // 光标移到新节点开头
	            selection.removeAllRanges();
	            selection.addRange(rangeNew);

	            // 滚动到新节点位置
	            const rect = newNode.getBoundingClientRect();
	            const editorRect = fullPageEditor.getBoundingClientRect();
	            const scrollTop = fullPageEditor.scrollTop;
	            const relativeTop = rect.top - editorRect.top + scrollTop;

	            if (rect.bottom > editorRect.bottom) {
	                fullPageEditor.scrollTop = relativeTop - editorRect.height + rect.height + 10;
	            } else if (rect.top < editorRect.top) {
	                fullPageEditor.scrollTop = relativeTop - 10;
	            }

	            fullPageEditor.focus();
	            this.syncContent();
	        } else if (e.key === 'Tab') {
	            e.preventDefault();
	            const selection = window.getSelection();
	            if (selection.rangeCount > 0) {
	                const range = selection.getRangeAt(0);
	                const currentBlock = range.startContainer.nodeType === 1 
	                    ? range.startContainer 
	                    : range.startContainer.parentElement;

	                const li = currentBlock.closest('li');
	                if (li) {
	                    const parentUlOrOl = li.parentElement;
	                    const isNested = parentUlOrOl.parentElement.tagName === 'LI';

	                    if (!isNested && li.previousElementSibling) {
	                        const subList = document.createElement(parentUlOrOl.tagName);
	                        subList.appendChild(li.cloneNode(true));
	                        li.previousElementSibling.appendChild(subList);
	                        li.remove();
	                        const newLi = subList.querySelector('li');
	                        range.setStart(newLi, 0);
	                        range.setEnd(newLi, 0);
	                        selection.removeAllRanges();
	                        selection.addRange(range);
	                    } else {
	                        document.execCommand('indent', false, null);
	                    }
	                } else {
	                    document.execCommand('insertText', false, '    ');
	                }
	            }
	            this.syncContent();
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

        colsInput.focus();

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
        const table = document.createElement('table');
        const tbody = document.createElement('tbody');
        for (let i = 0; i < rows; i++) {
            const tr = document.createElement('tr');
            for (let j = 0; j < cols; j++) {
                const td = document.createElement('td');
                tr.appendChild(td);
            }
            tbody.appendChild(tr);
        }
        table.appendChild(tbody);

        const targetEditor = this.isFullPage ? 
            this.fullPageContainer.querySelector('.rich-editor-content') : this.visualEditor;
        targetEditor.focus();
        targetEditor.appendChild(table);
        this.syncContent();
    }

    setTableBorderColor(color) {
        const targetEditor = this.isFullPage ? 
            this.fullPageContainer.querySelector('.rich-editor-content') : this.visualEditor;
        const tables = targetEditor.querySelectorAll('table');
        tables.forEach(table => {
            table.style.borderColor = color;
            const cells = table.querySelectorAll('td, th');
            cells.forEach(cell => cell.style.borderColor = color);
        });
    }

    setTableBorderWidth(width) {
        const targetEditor = this.isFullPage ? 
            this.fullPageContainer.querySelector('.rich-editor-content') : this.visualEditor;
        const tables = targetEditor.querySelectorAll('table');
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
                const targetEditor = this.isFullPage ? 
                    this.fullPageContainer.querySelector('.rich-editor-content') : this.visualEditor;
                targetEditor.appendChild(img);
                this.syncContent();
            };
            reader.readAsDataURL(file);
        }
    }

    syncContent() {
        const isHtmlMode = this.htmlEditor.style.display === 'block';
        let content;
        if (isHtmlMode) {
            content = this.htmlEditor.value;
        } else if (this.isFullPage) {
            content = this.fullPageContainer.querySelector('.rich-editor-content').innerHTML;
            this.visualEditor.innerHTML = content; // 同步到原始编辑器
        } else {
            content = this.visualEditor.innerHTML;
        }
        this.targetElement.value = content;
        
        if (this.options.onChange) {
            this.options.onChange(content);
        }
    }

    formatHtml(html) {
        let formatted = '';
        let indent = 0;
        const tab = '    ';
        
        html = html.trim();
        if (!html) return '';

        html.split(/>\s*</).forEach(function(element) {
            if (element.match(/^\/\w/)) {
                indent--;
            }
            const safeIndent = Math.max(0, indent);
            if (indent < 0) {
                console.warn(`Indent became negative: ${indent}, element: ${element}`);
            }
            formatted += tab.repeat(safeIndent) + '<' + element + '>\n';
            if (element.match(/^<?\w[^>]*[^\/]$/) && !element.startsWith("input")) {
                indent++;
            }
        });

        const result = formatted.substring(1, formatted.length - 3);
        return result || html;
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
        if (this.fullPageContainer) {
            document.body.removeChild(this.fullPageContainer);
        }
        this.wrapper.remove();
        this.targetElement.style.display = '';
    }
}