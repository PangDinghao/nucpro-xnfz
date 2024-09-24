<template>
  <div>
    <div style="margin-left: 30px;margin-top: 0;margin-bottom: 0; display: flex;width: auto">
      <p style="font-size: 20px">新闻公告管理</p>
      <div style="margin-top: 20px;margin-bottom: 0px;margin-left: auto;" >
        <el-date-picker
              v-model="createDatetime"
              align="right"
              type="date"
              placeholder="选择发布日期"
              :picker-options="pickerOptions"
              @change="loadPost"
              :clearable="false"
              style="width: 150px"

        >
        </el-date-picker>
        <el-input
            placeholder="请输入标题关键字"
            suffix-icon="el-icon-search"
            v-model="title" style="width: 200px;margin-left: 10px"
            @keyup.enter.native="loadPost"
        >
        </el-input>
        <el-input
              placeholder="请输入作者关键字"
              suffix-icon="el-icon-search"
              v-model="author" style="width: 170px;margin-left: 10px"
              @keyup.enter.native="loadPost"
          >
        </el-input>
        <el-button type="success" style="margin-left: 30px" @click="add">发布</el-button>
        <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
        <el-button type="success" @click="resetParam">重置</el-button>
      </div>
    </div>

      <div style="margin-top:0px;margin-left:20px;width: 1000px;">
        <el-table :data="tableData" :show-header="false">

          <el-table-column  width="810px"><!--第一列-->
            <template slot-scope="scope">
             <div>
              <!-- 第一行 -->
               <p>{{ scope.row.title }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                作者：{{ scope.row.author }}&nbsp;&nbsp;&nbsp;&nbsp;
                是否置顶：
                 <el-tag
                     :type="scope.row.noticeType == 1 ?  'succes' : 'info'"
                     disable-transitions>{{scope.row.noticeType== 1 ? '是' : '否'}}</el-tag>


              </p>
              <!-- 第二行 -->
              <p>
                点击量：{{scope.row.hits}} &nbsp;&nbsp;&nbsp;&nbsp;
                发布时间：{{scope.row.createDatetime}} &nbsp;&nbsp;&nbsp;&nbsp;
                最近修改时间：{{scope.row.updateDatetime}} &nbsp;&nbsp;&nbsp;&nbsp;
                最近修改者：{{scope.row.updateUser}} &nbsp;&nbsp;&nbsp;&nbsp;
              </p>
            </div>
            </template>
          </el-table-column>

          <el-table-column width="50px">
            <template slot-scope="scope">
              <el-link type="success" @click="update(scope.row)">修改</el-link>
            </template>
          </el-table-column>
          <el-table-column width="50px">
            <template slot-scope="scope">
              <el-popconfirm
                  title="确定删除吗？"
                  @confirm="remove(scope.row)"
              >
                <el-link type="danger" slot="reference">删除</el-link>
              </el-popconfirm>
            </template>
          </el-table-column>
          <el-table-column width="70px">
            <template slot-scope="scope">
              <el-link type="info" @click="detail(scope.row)">查看<i class="el-icon-view el-icon--right"></i></el-link>
            </template>
          </el-table-column>



        </el-table>
        <div style="display: flex; justify-content: center;">
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page= "pageNum"
              :page-sizes="[4,5, 10, 20]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
          </el-pagination>
        </div>
      </div>

    <el-dialog
        title="提示"
        :visible.sync="DialogVisible"
        width="50%"
        style="margin-top: -88px"
        center>
      <el-form ref="form" :model="form" label-width="80px" style="display: grid;place-items: center;">
        <el-form-item label="标题" prop="no">
          <el-col :span="35">
            <el-input  v-model="form.title" style="width: 400px"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="详情" prop="details">
          <el-col :span="35">
            <el-input type="textarea" v-model="form.details":rows="10" style="width: 400px;"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="是否置顶" prop="noticeType">
          <el-col :span="35">
            <el-switch v-model="switchValue"></el-switch>
          </el-col>
        </el-form-item>

        <el-form-item label="封面图片" prop="photo">
          <el-upload
              action="http://localhost:8083/file/upload"
              :on-success="handle_success"
          >
            <!-- 再次提醒，:on-success="函数名"，函数名后面不要跟括号 -->
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传jpg/png文件，且不超过500kb
            </div>
          </el-upload>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="DialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>
  </div>




</template>

<script>

export default {
  name: "Notice_Manage",
  data() {
    return{
      Curuser:JSON.parse(sessionStorage.getItem('CurUser')),//当前用户类
      DialogVisible:false,//提示窗口是否显示
      tableData: [],
      topTableData: [],
      pageSize:4,
      pageNum:1,
      total:0,
      title:'',
      author:'',
      ImgApiUrl:'',
      form:{
        no:'',
        title:'',
        author:'',
        hits:'',
        details: '',
        coverImg: '',
        createDatetime:'',
        updateDatetime:'',
        updateUser:'',
        noticeType:''
      },
      createDatetime:'',
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }]
      },
    }
  },
  created() {
    this.loadPost();
    this.loadPosttopTableData();
  },
  methods:{
    handle_success(res) {//图片上传成功过之后将图片名字传至coverImg
      console.log(res);
      this.$message.success("图片上传成功");
      return (this.form.coverImg = res.data);
    },
    loadPost() {
      this.$axios.post(this.$httpUrl+'/notice/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          title:this.title,
          author: this.author,
          createDatetime:this.createDatetime.toString(),
        }

      }).then(res => res.data).then(res => {
        console.log(res);
        if (res.code == 200) {

          this.tableData = res.data;
          this.total = res.total;

        } else {
          alert("获取数据失败");
        }

      })
    },
    loadPosttopTableData() {
      this.$axios.get(this.$httpUrl+'/notice/toplist').then(res => res.data).then(res => {
        console.log(res);
        if (res.code == 200) {
          this.topTableData = res.data;
        } else {
          alert("获取数据失败");
        }
      })
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageSize=val;
      this.loadPost();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum=val;
      this.loadPost();
    },
    detail(notice){
      this.$axios.post(this.$httpUrl+'/notice/hit', notice).then(res => res.data).then(res => {
        console.log(res);
        if (res.code == 200) {
          sessionStorage.setItem("notice",JSON.stringify(notice))//当前对象封装到session中
          sessionStorage.setItem("Curuser",JSON.stringify(this.Curuser))//当前访问的用户
          this.$router.push('Notice_DetailManage');

        } else {
          alert("获取数据失败");
        }

      })

    },

    save() {

      if (this.form.no) {
        this.doUpdate();
      } else {
        this.doAdd();
      }

      this.restFrom();
      this.loadPost();
    },
    update(notice){
      this.form = notice;
      this.form.updateUser = this.Curuser.username;

      this.DialogVisible=true;

    },
    doUpdate(){

      this.$axios.post(this.$httpUrl+'/notice/update',this.form).then(res=>res.data).then(res=>{
        console.log(res);

        if(res.code==200){

          this.$message({
            message: '修改成功',
            type: 'success',
          });
          this.loadPost()
          this.DialogVisible=false;

        }else{
          this.$message({
            message: '操作失败',
            type: 'error'
          });
        }
      })




    },
    add(){
      this.DialogVisible=true;

      this.form.author = this.Curuser.username;
      this.form.updateUser = this.Curuser.username;
    },
    doAdd(){
      this.$axios.post(this.$httpUrl+'/notice/save',this.form).then(res=>res.data).then(res=>{
        console.log(res);

        if(res.code==200){

          this.$message({
            message: '修改成功',
            type: 'success',
          });
          this.loadPost();
          this.DialogVisible=false;

        }else{
          this.$message({
            message: '操作失败',
            type: 'error'
          });
        }
      })


    },
    remove(notice){
      this.$axios.get(this.$httpUrl+`/notice/remove?no=${notice.no}`).then(res=>res.data).then(res=>{
        console.log(res);

        if(res.code==200){

          this.$message({
            message: '删除成功',
            type: 'success',
          });
          this.loadPost()

        }else{
          this.$message({
            message: '操作失败',
            type: 'error'
          });
        }
      })
      this.restFrom();

    },
    resetParam(){
      this.title = '';
      this.author = '';
      this.createDatetime = '';
      this.loadPost();
    },
    restFrom() {
      this.form={
            no:'',
            title:'',
            author:'',
            hits:'',
            details: '',
            coverImg: '',
            createDatetime:'',
            updateDatetime:'',
            updateUser:'',
            noticeType:''
      }
    },
  },
  computed: {
    switchValue: {
      get() {
        return this.form.noticeType == 1; // 将1转换为true，0转换为false
      },
      set(value) {
        this.form.noticeType = value ? 1 : 0; // 将true转换为1，false转换为0
      }
    },
    beforeMount() {
      this.loadPost();
      this.loadPosttopTableData();
    },
  }
}
</script>

<style scoped>

</style>