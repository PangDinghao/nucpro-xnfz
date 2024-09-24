<template>
  <div>
    <div style="margin-left: 30px;margin-top: 0;margin-bottom: 0; display: flex;width: auto">
      <p style="font-size: 20px">实训计划管理</p>
      <div style="margin-top: 20px;margin-bottom: 0px;margin-left: auto;" >
        <el-date-picker
            v-model="planOpentime"
            align="right"
            type="date"
            placeholder="选择开始日期"
            :picker-options="pickerOptions"
            @change="loadPost"
            :clearable="false"
            style="width: 150px"
        >
        </el-date-picker>
        <el-input
            placeholder="实训计划名"
            suffix-icon="el-icon-search"
            v-model="planname" style="width: 100px;margin-left: 10px"
            @keyup.enter.native="loadPost"
            @change="loadPost"
        >
        </el-input>
        <el-select v-model="userid" style="width: 120px;margin-left: 10px" @change="loadPost" placeholder="负责教师">
          <el-option
              v-for="item in TeacherData"
              :key="item.userid"
              :label="item.username"
              :value="item.userid">
          </el-option>
        </el-select>

        <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
        <el-button type="success" @click="resetParam">重置</el-button>
      </div>
    </div>

    <div style="margin-top:0px;margin-left:20px;width: 1000px;">
      <el-table :data="tableData" :show-header="false">

        <el-table-column  width="770px">
          <template slot-scope="scope">
            <div>
              <!-- 第一行 -->
              <p>
                实训计划名：{{ scope.row.planName }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                实训包含课程
                <!-- 遍历 roleList 数组 -->
                <el-dropdown trigger="click" placement="bottom-start">
                  <i class="el-icon-arrow-down el-icon--right"></i>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-for="course in scope.row.courseList" :key="course.courseid">
                      {{ course.coursename }}
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                负责教师：{{ teacherName(scope.row.userid) }}
              </p>
              <!-- 第二行 -->
              <p>
                实训目标：{{scope.row.planGoal}} &nbsp;&nbsp;&nbsp;&nbsp;
                开始日期：{{scope.row.planOpentime | truncateDate}} &nbsp;&nbsp;&nbsp;&nbsp;
                结束日期：{{scope.row.planEndtime | truncateDate}} &nbsp;&nbsp;&nbsp;&nbsp;
              </p>
            </div>
          </template>
        </el-table-column>

        <el-table-column width="110px">
          <template slot-scope="scope">
            <el-link type="success" @click="ToClassManage(scope.row)">查看参与班级</el-link>
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

      <el-form ref="form" :model="form" label-width="80px" style="display: grid;place-items: center;" >

        <el-form-item label="计划名字" prop="planName">
          <el-col :span="35">
            <el-input  v-model="form.planName" style="width: 400px"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="实训目标" prop="planGoal">
          <el-col :span="35">
            <el-input v-model="form.planGoal":rows="10" style="width: 400px;"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="负责教师" prop="userid">
          <el-select v-model="form.userid" filterable  placeholder="请选择代课教师">
            <el-option
                v-for="teacher in TeacherData"
                :key="teacher.userid"
                :label="teacher.username"
                :value="teacher.userid">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="planOpentime">
          <el-date-picker
              align="right"
              type="date"
              placeholder="选择开始日期"
              :picker-options="pickerOptions"
              v-model="form.planOpentime"
              :clearable="false"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期" prop="planEndtime">
          <el-date-picker
              align="right"
              type="date"
              placeholder="选择开始日期"
              :picker-options="pickerOptions"
              v-model="form.planEndtime"
              :clearable="false"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="包含课程" prop="courselist">
          <el-select v-model="selectedCourseIds"  filterable multiple  placeholder="请选择包含课程" @change="changeSelectedCourseIds()">
            <el-option
                v-for="course in CourseData"
                :key="course.courseid"
                :label="course.coursename"
                :value="course.courseid">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="resetForm">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog><!--新增修改弹窗-->
  </div>




</template>

<script>

export default {
  name: "Notice_Manage",
  filters: {
    truncateDate(value) {//只显示固定位数的年月日
      if (value) {
        return value.substring(0, 10);
      } else {
        return '';
      }
    }
  },
  data() {
    return{
      TeacherData:[],//教师
      CourseData: [],//课程
      selectedCourseIds:[],//待添加/修改的课程的id集合
      Curuser:JSON.parse(sessionStorage.getItem('CurUser')),//当前用户类
      DialogVisible:false,//提示窗口是否显示
      tableData: [],
      pageSize:4,
      pageNum:1,
      total:0,
      userid:'',
      planname:'',
      planOpentime:'',
      index:'',

      form:{
        planid:'',
        planName:'',
        planGoal: '',
        planOpentime: '',
        planEndtime: '',
        userid:'',//负责教师
        courseList:[]
      },

      pickerOptions: {
        disabledDate(time) {
          return time.getTime() == Date.now();
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
    this.getTeacherUser();
    this.getCourse();
  },
  methods:{
    loadPost() {
      this.$axios.post(this.$httpUrl+'/plan/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          planname:this.planname,
          userid: this.userid,
          planOpentime:this.planOpentime.toString(),
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
    async getTeacherUser() {//获取到教师用户
      try {
        const res = await this.$axios.get(this.$httpUrl+'/user/listByRoleid?roleid=2');
        console.log("教师用户",res);
        if (res.data.code == 200) {
          this.TeacherData = res.data.data;
        } else {
          throw new Error("获取数据失败");
        }
      } catch (error) {
        console.error("获取用户数据失败", error);
        throw error;
      }
    },//获取教师数据
    getCourse() {
      this.$axios.post(this.$httpUrl+'/course/listAll').then(res => res.data).then(res => {
        console.log("课程",res);
        if (res.code == 200) {

          this.CourseData = res.data;

        } else {
          alert("获取数据失败");
        }

      })
    },//获取课程数据
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

    save() {
      //转化时间格式
      const dateFormat = /^\d{4}-\d{2}-\d{2}$/;

      if (!dateFormat.test(this.form.planOpentime)) {
        // If not in the correct format, adjust the format
        this.form.planOpentime = `${this.form.planOpentime.getFullYear()}-${(this.form.planOpentime.getMonth() + 1).toString().padStart(2, '0')}-${this.form.planOpentime.getDate().toString().padStart(2, '0')}`;
      }

      if (!dateFormat.test(this.form.planEndtime)) {
        this.form.planEndtime = `${this.form.planEndtime.getFullYear()}-${(this.form.planEndtime.getMonth() + 1).toString().padStart(2, '0')}-${this.form.planEndtime.getDate().toString().padStart(2, '0')}`;
      }
      console.log("表单",this.form)
      if (this.form.planid) {
        this.doUpdate();
      } else {
        this.doAdd();
      }
    },
    update(trainingplan){
      this.form = trainingplan;

      this.updateSelectedRoleIds(this.form.courseList);//将form.userList中每一项的userid存入

      this.loadPost();

      this.DialogVisible=true;

    },
    doUpdate(){
      this.$axios.post(this.$httpUrl+'/plan/update',this.form).then(res=>res.data).then(res=>{
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
      this.resetForm();

    },
    add(){

      this.resetForm();
      this.DialogVisible=true;
    },
    doAdd(){
      console.log("新增",this.form)
      this.$axios.post(this.$httpUrl+'/plan/save', this.form).then(res=>res.data).then(res=>{
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
      this.resetForm();
    },
    remove(trainingplan){
      this.$axios.get(this.$httpUrl+`/plan/remove?trainingplanid=${trainingplan.planid}`).then(res=>res.data).then(res=>{
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
    },
    resetForm(){//重置表单信息
      this.DialogVisible = false;

      this.form = {
        planid:'',
        planName:'',
        planGoal: '',
        planOpentime: '',
        planEndtime: '',
        userid:'',//负责教师
        courseList:[]
      };
      this.selectedCourseIds = [];

      this.loadPost();

    },
    resetParam(){//重置搜索栏

      this.form = {
        planid:'',
        planName:'',
        planGoal: '',
        planOpentime: '',
        planEndtime: '',
        userid:'',//负责教师
        courseList:[]
      };
      this.userid = '';
      this.planOpentime = '';
      this.planname = '';
      this.loadPost();
    },
    updateSelectedRoleIds(CourseIds) {//将form.courseList中的courseid都传给selectedCourseIds数组
      this.selectedCourseIds = CourseIds.map(course => course.courseid);

      this.form.courseList = [];

      // 遍历courseid数组
      this.selectedCourseIds.forEach(courseId => {
        // 在courseData中查找匹配的courseid
        const course = this.CourseData.find(option => option.courseid === CourseIds);
        // 如果找到了匹配的course对象，则将其添加到form.courseList中
        if (course) {
          this.form.courseList.push(course);
        }
      });
    },
    changeSelectedCourseIds() {//将selectedCourseIds中courseid对应的course存入form.courseList
      this.form.courseList = this.selectedCourseIds.map(courseId => {
        return this.CourseData.find(course => course.courseid === courseId);
      })
    },
    ToClassManage(plan){
      sessionStorage.setItem("plan",JSON.stringify(plan))
      this.$router.push("TrainingPlan_TeacherManage");
    }



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
    teacherName(userid) {//返回教师用户名字
      return (userid) => {
        if (!userid || this.TeacherData.length === 0) {
          return '';
        }
        const teacher = this.TeacherData.find(user => user.userid === userid);
        return teacher ? teacher.username : '';
      };
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