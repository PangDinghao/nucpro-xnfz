<template>
  <div>
    <div style="margin-left: 30px;margin-top: 0;margin-bottom: 0; display: flex;width: auto">
      <p style="font-size: 20px">新闻公告</p>
      <div style="margin-top: 20px;margin-bottom: 20px;margin-left: auto" >
        <el-input
            placeholder="请输入标题关键字"
            suffix-icon="el-icon-search"
            v-model="title" style="width: 200px"
            @keyup.enter.native="loadPost"
        >
        </el-input>
        <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
        <el-button type="success" @click="resetParam">重置</el-button>
      </div>
    </div>
    <div style="margin: 20px">
      <template>
        <el-carousel :interval="4000" type="card" height="200px">
          <el-carousel-item v-for="item in topTableData" :key="item.no">
            <el-image :src="require('D://A_vue/images/' + item.coverImg)" @click="detail(item)"></el-image>

          </el-carousel-item>
        </el-carousel>
      </template>
    </div>

    <div style="margin:20px ;width: 1000px;">
      <el-table :data="tableData">

        <el-table-column prop="title"  width="600">
        </el-table-column>
        <el-table-column prop="author"  width="140" >
          <template slot-scope="scope">
            <span style="text-align: center;">作者：{{ scope.row.author }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="hits"  width="140">
          <template slot-scope="scope">
            <span>点击量：{{ scope.row.hits }}</span>
          </template>
        </el-table-column>

        <el-table-column>
          <template slot-scope="scope">
            <el-button type="info" plain @click="detail(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>



      </el-table>
      <div style="display: flex; justify-content: center;">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page= "pageNum"
            :page-sizes="[3, 5, 10, 20]"
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
  name: "Notice",

  data() {
    return{
      tableData: [],
      topTableData: [],
      pageSize:3,
      pageNum:1,
      total:0,
      title:'',
      ImgApiUrl:'',
      form:{
        no:'',
        title:'',
        author:'',
        hits:'',
        details: '',
        coverImg: '',
        createDatetime:''
      }
    }
  },
  created() {
    this.loadPost();
    this.loadPosttopTableData();
  },
  methods:{
    loadPost() {
      this.$axios.post(this.$httpUrl+'/notice/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          title:this.title,
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
          this.$router.replace('/Notice_Detail')

        } else {
          alert("获取数据失败");
        }

      })

    },
    resetParam(){
      this.title='';
      this.loadPost();
    }
  },

  beforeMount() {
    this.loadPost();
    this.loadPosttopTableData();
  },

}
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}

</style>