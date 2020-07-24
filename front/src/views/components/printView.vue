<template>
  <div class="print">
    <iframe id="iframe" style="display: none;"></iframe>
  </div>
</template>
<script>
export default {
  name: "print",
  props: {
    html: {
      type: String,
      default: ""
    }
  },
  watch: {
    html(val) {
      if (val) {
        this.setBodyHtml(val);
      }
    }
  },
  methods: {
    setBodyHtml(html) {
      const document = window.document;
      const iframe = window.frames[0];
      iframe.document.head.innerHTML = document.head.innerHTML; // 获取当前文档的头部给iframe
      iframe.document.body.innerHTML = html; // 把传过来的html给iframe头部

console.log(111)
      // 图片和样式加载完成
      Promise.all([this.loadStyle(), this.loadImage()]).then(res => {
          console.log(res)
        //   打印
        iframe.window.print();
      });
    },
    loadStyle() {
      const iframe = window.frames[0];
      const styles = iframe.document.head.getElementsByTagName("style"); // <style>
      const links = iframe.document.head.getElementsByTagName("link"); // <link>
      let arrs = [];
      arrs = arrs.concat(...styles, ...links);
      console.log(arrs)

      return new Promise((resolve, reject) => {
        for (let i = 0; i < arrs.length; i++) {
          arrs[i].onload = function() {
            if (i === arrs.length - 1) {
              console.log("style 样式加载完成");
              resolve("style 样式加载完成");
            }
          };
        }
      });
    },
    loadImage() {
      const iframe = window.frames[0];
      const imgs = iframe.document.body.getElementsByTagName("img"); // <img>
      
      return new Promise((resolve, reject) => {
        for (let i = 0; i < imgs.length; i++) {
          imgs[i].onload = function() {
            if (i === imgs.length - 1) {
              console.log("img 加载完成");
              resolve("img 加载完成");
            }
          };
        }
      });
    }
  }
};
</script>
