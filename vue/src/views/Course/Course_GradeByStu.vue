<template>
  <div>
    <div style="margin-left: 30px;margin-top: 0;margin-bottom: 0; display: flex;width: auto">
      <p style="font-size: 20px">我的成绩</p>

    </div>

    <div style="margin-top:0px;margin-left:20px;width: 1000px;">
      <el-table :data="tableData" :show-header="false">

        <el-table-column  width="950px">
          <template slot-scope="scope">
            <div>
              <!-- 第一行 -->
              <p class="course-info">
                <span style="margin-left: 30px">课程名：{{ scope.row.coursename }}</span>
                <span style="margin-left: 65%">成绩：{{ scope.row.grade }}</span>
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
  name: "Notice_Manage",
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
    loadPost() {
      this.$axios.post(this.$httpUrl+'/course/getCourseByStu', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          userid: this.Curuser.userid,
        }
      }).then(res => res.data).then(res => {
        console.log("当前课程",res);
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