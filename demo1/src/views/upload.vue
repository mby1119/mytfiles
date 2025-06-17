<template>
  <div class="upload__wrap" :class="`upload__wrap--${size}`">
    <div class="files" v-for="img in existsImgs" :key="img.id">
      <template v-if="pictureType.includes(handleType(img.fileSuffix))">
        <!-- 图片类型 -->
        <img style="object-fit: cover;" width="104" height="104" :src="handleImg(img.id, 208, 208)" />
        <div class="btn__wraps">
          <div class="btn__innerwraps">
            <a-icon class="icon__btn" type="eye" @click="$refs.previewForm.preview({ id: img.id })" />
            <a-popconfirm placement="topRight" title="确认删除？" @confirm="() => deleteImg(img.id)">
              <a-icon class="icon__btn" type="delete" />
            </a-popconfirm>
          </div>
        </div>
      </template>
      <template v-else-if="threedType.includes(handleType(img.fileSuffix))">
        <img
            style="object-fit: cover;cursor: pointer;"
            width="104"
            height="104"
            src="https://aimilin.com/stata/test.png"
        />
        <div class="btn__wraps">
          <div class="btn__innerwraps">
            <a-icon class="icon__btn" type="eye" @click="show3dModal(img)" />
            <a-popconfirm placement="topRight" title="确认删除？" @confirm="() => deleteImg(img.id)">
              <a-icon class="icon__btn" type="delete" />
            </a-popconfirm>
          </div>
        </div>
      </template>
      <template v-else>
        当前类型文件暂不支持预览
      </template>
    </div>
    <div class="tempimg__placeholder" v-for="temp in tempImgArr" :key="temp.uid">上传中…</div>
    <a-upload
        name="upload"
        :list-type="listType"
        :file-list="fileList"
        :accept="format"
        :multiple="multiple"
        :before-upload="beforeUpload"
        :customRequest="customRequest"
    >
      <div v-if="existsImgs.length + tempImgArr.length < maxPicsLength">
        <a-icon type="plus" />
        <div class="ant-upload-text">
          上传
        </div>
      </div>
    </a-upload>
    <preview-form ref="previewForm"></preview-form>
    <preview3d-model :is3dModelShow="is3dModelShow" :carousel-lists="preview3dModel" title="3D模型预览" @closeModal="closeModal"></preview3d-model>
    <!-- :carousel-lists="" -->
  </div>
</template>

<script>
import { sysFileInfoPartUpload } from '@/api/modular/system/fileManage'
import previewForm from '@/views/system/file/previewForm.vue'
import Preview3dModel from '@/views/system/file/preview3dmodel.vue'
import { handleImg } from '@/utils/util'
import SparkMD5 from 'spark-md5'
import { SUCCESS, SERVICE_ERROR, UPLOADING } from '@/assets/js/responseCode'

const SIZEUNIT = 1 * 1024 * 1024
export default {
  components: {
    previewForm,
    Preview3dModel
  },
  props: {
    isCloseUpload: {
      type: Boolean,
      default: false
    },
    size: {
      type: String,
      default: 'default'
    },
    format: {
      type: String,
      default: 'image/gif, image/jpeg, image/png, image/jpg'
    },
    listType: {
      type: String,
      default: 'picture-card'
    },
    maxPicsLength: {
      type: Number,
      default: 9
    },
    uploadText: {
      type: String,
      default: '上传'
    },
    existsImgs: {
      type: Array,
      default () {
        return []
      }
    },
    maxSize: {
      type: Number,
      default: 20
    },
    multiple: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      pictureType: ['.gif', '.jpeg', '.png', '.jpg'],
      threedType: ['.json', '.obj', '.dae', '.ply', '.gltf', '.stl', '.fbx'],
      previewVisible: false,
      previewImage: '',
      fileList: [],
      // loading: false,
      is3dModelShow: false,
      preview3dModel: [],
      tempImgArr: [],
      isStopUpload: false
    }
  },
  create() {
    this.timer = null
    console.log('this', this)
  },
  watch: {
    isCloseUpload: {
      handler (newval) {
        if (newval) {
          this.$set(this, 'tempImgArr', [])
          this.$emit('imgUploadingStatus', 0)
        }
      },
      immediate: true
    }
  },
  methods: {
    handleImg,
    show3dModal (obj) {
      this.preview3dModel = [obj]
      this.is3dModelShow = true
    },
    closeModal () {
      this.is3dModelShow = false
    },
    handleType (filetType) {
      return filetType.indexOf('.') > -1 ? filetType : '.' + filetType
    },
    beforeUpload(file, fileList) {
      console.log('this', this)
      return new Promise((resolve, reject) => {
        let type = file.type
        if (!type) {
          type = '.' + file.name.split('.').pop()
        }
        const isFormatFiles = this.format.replace(/\s*/g, '').split(',').includes(type)
        if (!isFormatFiles) {
          this.$message.error(`只支持以下${this.format}格式！`)
          return reject(new Error(`只支持以下${this.format}格式！`))
        }
        const maxSizeLimit = this.threedType.includes(type) ? 100 : 20
        const isLtMaxSize = file.size / SIZEUNIT < maxSizeLimit
        if (!isLtMaxSize) {
          this.$message.error(`图片须小于${maxSizeLimit}MB!`)
          return reject(new Error(`图片须小于${maxSizeLimit}MB!`))
        }
        // 是否上传图片超过最大限度
        if (this.existsImgs.length + this.tempImgArr.length >= this.maxPicsLength) {
          if (this.timer) {
            clearTimeout(this.timer)
          }
          this.timer = setTimeout(() => {
            this.$message.error(`最多只能上传${this.maxPicsLength}张！`)
          }, 300)
          return reject(new Error(`最多只能上传${this.maxPicsLength}张！`))
        }
        this.isStopUpload = false
        // this.loading = true
        this.$set(this, 'tempImgArr', [...this.tempImgArr, file.uid])
        this.$emit('imgUploadingStatus', [...this.tempImgArr, file.uid].length)
        this.$emit('resetUploadStatus')
        resolve(true)
      })
      // return isFormatFiles && isLt2M
    },
    preview (id) {
      this.$refs.previewForm.preview({ id })
    },
    deleteImg (id) {
      this.$emit('deletePic', id)
    },
    /**
     * 上传文件
     */
    customRequest (data) {
      const fileType = '.' + data.file.name.split('.').pop()
      const fileReader = new FileReader()
      const blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice
      let currentChunk = 0
      const chunkSize = 4 * 1024 * 1024
      const chunks = Math.ceil(data.file.size / chunkSize)
      const spark = new SparkMD5.ArrayBuffer()
      const partChunksArr = []
      const fileData = {}
      loadNext()
      fileReader.onload = e => {
        spark.append(e.target.result)
        const sparkChunk = new SparkMD5.ArrayBuffer()
        sparkChunk.append(e.target.result)
        const partMd5 = sparkChunk.end()
        partChunksArr.push({
          file: fileData[currentChunk],
          partNumber: currentChunk + 1,
          partMd5,
          partSize: chunkSize,
          totalSize: data.file.size
        })
        currentChunk++
        if (currentChunk < chunks) {
          loadNext()
        } else {
          const md5 = spark.end()
          this.finalUploadFn(partChunksArr, fileType, data, md5)
        }
      }

      fileReader.onerror = function () {
        this.$message.error(`文件${data.file.name}读取出错，请检查该文件`)
        // data.cancel()
      }

      function loadNext() {
        const start = currentChunk * chunkSize
        const end = ((start + chunkSize) >= data.file.size) ? data.file.size : start + chunkSize
        const currentChunkData = blobSlice.call(data.file, start, end)
        fileReader.readAsArrayBuffer(currentChunkData)
        fileData[currentChunk] = currentChunkData
      }
    },
    finalUploadFn (formData, fileType, data, wholeFileMd5) {
      formData.forEach(item => {
        const newFormData = new FormData()
        // newFormData.set('file', data.file)
        newFormData.set('uid', data.file.uid)
        newFormData.set('filename', data.file.name)
        Object.keys(item).forEach(key => {
          newFormData.set(
              key, item[key]
          )
          newFormData.set('fileMd5', wholeFileMd5)
        })
        if (this.isStopUpload) {
          return
        }
        sysFileInfoPartUpload(newFormData).then((res) => {
          // this.loading = false
          if (res.code === SUCCESS && res.data?.fileState === SUCCESS) {
            this.$emit('getNewPics', {
              id: res.data.fileId,
              fileSuffix: fileType
            })
            const newTempImgArr = this.tempImgArr.filter(item => item !== res.data?.uid)
            this.$set(this, 'tempImgArr', newTempImgArr)
            this.$emit('imgUploadingStatus', newTempImgArr.length)
            // this.$refs.table.refresh()
          } else if (res.code === SUCCESS && res.data?.fileState === UPLOADING) {
          } else if (res.code === SUCCESS && res.data?.fileState === SERVICE_ERROR) {
            if (!this.failupload) {
              this.failupload = {}
              this.failupload[data.file.uid] = data.file.uid
              sysFileInfoPartUpload(newFormData)
            } else {
              if (!this.failupload[data.file.uid]) {
                sysFileInfoPartUpload(newFormData)
                this.failupload[data.file.uid] = data.file.uid
              }
            }
          } else if (res.code !== SUCCESS) {
            // 上传失败，从占位图中移除一个
            const newTempImgArr = this.tempImgArr
            newTempImgArr.pop()
            this.$set(this, 'tempImgArr', newTempImgArr)
            this.$emit('imgUploadingStatus', newTempImgArr.length)
            if (this.timer) {
              clearTimeout(this.timer)
            }
            this.timer = setTimeout(() => {
              this.$message.error('上传失败！' + res.message)
            }, 300)
          }
        }).catch(e => {
          const newTempImgArr = this.tempImgArr
          newTempImgArr.pop()
          this.$set(this, 'tempImgArr', newTempImgArr)
          this.$emit('imgUploadingStatus', newTempImgArr.length)
          console.log('error', e)
          // this.loading = false
          // this.tempImgArr.length && this.$message.error('上传失败，请重新上传')
        }).finally((p) => {
          console.log('sysFileInfoPartUpload', p)
          // this.loading = false
        })
      })
    },
    clearTimer() {
      clearTimeout(this.timer)
      this.$set(this, 'tempImgArr', [])
      this.$emit('imgUploadingStatus', 0)
      this.isStopUpload = true
    }
  },
  beforeDestoryed() {
    this.clearTimer()
  }
}
</script>

<style>
/* you can make up upload button and sample style by using stylesheets */
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>

<style lang="less" scoped>
.upload__wrap{
  //display: -webkit-inline-box;
  display: -moz-inline-box;
  //display: inline-box;
  flex-wrap: wrap;
  .files{
    position: relative;
    width:104px;
    height: 104px;
    margin-right: 10px;
    margin-bottom: 10px;
    .btn__wraps{
      position: absolute;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      background: rgba(0,0,0,0);
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s linear;
      z-index: -1;
      .btn__innerwraps{
        display: flex;
        .icon__btn{
          margin-right: 10px;
          font-size: 16px;
          color: rgba(255,255,255, 0);
          cursor: pointer;
          &:last-child{
            margin-right: 0;
          }
        }
      }
    }
    &:hover{
      .btn__wraps{
        background: rgba(0,0,0,0.5);
        transition: all 0.3s linear;
        z-index: 1;
        .btn__innerwraps{
          .icon__btn{
            color: rgba(255,255,255, 0.8);
          }
        }
      }
    }
  }
  .tempimg__placeholder{
    width: 104px;
    height: 104px;
    display: flex;
    justify-content: center;
    align-items: center;
    border: 1px solid #d9d9d9;
    margin-right: 10px;
    margin-bottom: 10px;
  }
}

</style>

