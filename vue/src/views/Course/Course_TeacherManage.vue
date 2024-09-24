<template>
  <div>
    <div style="margin-left: 30px;margin-top: 0;margin-bottom: 0; display: flex;width: auto">
      <p style="font-size: 20px">课程安排</p>
    </div>

    <div style="margin-top:0px;margin-left:20px;width: 1000px;">
      <el-table :data="tableData" :show-header="false">

        <el-table-column  width="880px">
          <template slot-scope="scope">
            <div>
              <!-- 第一行 -->
              <p>
                <span>课程名：{{ scope.row.coursename }}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <span>  实验室安排
                  <!-- 遍历 roleList 数组 -->
                  <el-dropdown trigger="click" placement="bottom-start">
                    <i class="el-icon-arrow-down el-icon--right"></i>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item v-for="lab in scope.row.labList" :key="lab.labid">
                        {{ lab.labName }}|节次：{{ lab.courseSection }}|{{ lab.labLocation }}
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                  </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <span>课程简介：{{scope.row.coursedetail}}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <span>开创日期：{{scope.row.opentime}} </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              </p>

            </div>
          </template>
        </el-table-column>
        <el-table-column width="90px">
          <template slot-scope="scope">
            <el-link type="success" @click="ToStudent(scope.row)">查看学生</el-link>
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
  name: "Course_TeacherManage",
  data() {
    return{
      Curuser:JSON.parse(sessionStorage.getItem('CurUser')),//当前用户类
      tableData: [],
      pageSize:4,
      pageNum:1,
      total:0,
    }
  },
  created() {
    this.loadPost();
  },
  methods:{
    //获取课程及实验室安排
    loadPost() {
      this.$axios.post(this.$httpUrl+'/course/getCourseByTeacher', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          userid: this.Curuser.userid,
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
    ToStudent(Course) {
      sessionStorage.setItem("Course",JSON.stringify(Course))
      this.$router.push("Teacher_GradeByCourseManage");
    },

  },
  computed: {
    beforeMount() {
      this.loadPost();
      this.loadPosttopTableData();
    },
  },

}
</script>

<style scoped>

</style>