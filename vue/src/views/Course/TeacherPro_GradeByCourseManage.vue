<template>
  <div>
    <div style="margin-left: 30px;margin-top: 20px;margin-bottom: 0; display: flex;width: auto">

      <el-page-header @back="ToTeacher_GradeManage" content="成绩管理" style="width: 200px;"></el-page-header>
      <h1 style="margin: 0px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        {{ course.coursename }}</h1>


    </div>

    <div style="margin-top:0px;margin-left:20px;width: 1000px;">
      <el-table :data="tableData" :show-header="false">

        <el-table-column  width="880px">
          <template slot-scope="scope">
            <div>
              <!-- 第一行 -->
              <p>
                用户名：{{ scope.row.username }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                性别：
                <el-tag
                    :type="scope.row.sex == 1 ?  'success' : 'primary'"
                    disable-transitions>{{scope.row.sex== 1 ? '男' : '女'}}</el-tag>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                联系电话：{{ scope.row.phone }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                成绩：{{scope.row.grade}}
              </p>
            </div>
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


  </div>




</template>

<script>

export default {
  name: "TeacherPro_GradeByCourseManage.vue",
  data() {
    return{
      course:JSON.parse(sessionStorage.getItem('Course')),
      Curuser:JSON.parse(sessionStorage.getItem('CurUser')),//当前用户类
      DialogVisible:false,//提示窗口是否显示
      tableData: [],
      topTableData: [],
      pageSize:4,
      pageNum:1,
      total:0,
      username:'',
      phone:'',
      roleid:'',
      createDatetime:'',
      index:'',

      form:{
        userid: "",
        courseid: "",
        grade: "",
      },

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
  },
  methods:{
    ToTeacher_GradeManage(){
      this.$router.push("Course_TeacherProManage");
    },
    handle_success(res) {//图片上传成功过之后将图片名字传至coverImg
      console.log(res);
      this.$message.success("图片上传成功");
      alert(res.data)
      return (this.form.coverImg = res.data);
    },
    loadPost() {
      this.$axios.post(this.$httpUrl+'/user/getStuBycourseid', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          courseid: this.course.courseid,

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

    update(stu_course){
      this.form = stu_course;

      this.form.courseid = this.course.courseid;

      this.DialogVisible=true;

    },

    resetParam(){//重置搜索栏

      this.form = {
        userid:'',
        username:'',
        account:'',
        password:'',
        sex:'',
        email: '',
        phone: '',
        createDatetime:'',
        roleList:[]
      };
      this.loadPost();
    },


  },
  computed: {
    switchValue: {
      get() {
        return this.form.sex == 1; // 将1转换为true，0转换为false
      },
      set(value) {
        this.form.sex = value ? 1 : 0; // 将true转换为1，false转换为0
      }
    },
    beforeMount() {
      this.loadPost();
      this.loadPosttopTableData();
    },
  },

}
</script>

<style scoped>

</style>