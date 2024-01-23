<template>
    <div>
        <!-- 流星雨 -->
        <canvas id="stars"></canvas>
        <!-- 音乐 -->
        <div class="music-button" :class="{ playing: isMusicPlaying }" @click="toggleMusic">
            <i class="material-icons">{{ isMusicPlaying ? 'play' : 'pause' }}</i>
        </div>
        <!-- 标题 -->
        <span style="float: left; z-index: 999; font-size: xx-large; 
        position: absolute; left: 700px; top: 8px; color: #f3eeee;">我创建的投票</span>
        <i class="el-icon-s-custom" style="float: left; z-index: 999; font-size: xx-large; color: #eae2e2;
      position: absolute; left: 1230px; top: 30px;" @click="dialogVisiblePassword = true;"></i>
        <span style="float: left; z-index: 999; font-size: large; 
      position: absolute; left: 1270px; top: 36px; color: #f3eeee;">欢迎, {{ username }}</span>


        <!-- 背景以及导航栏 -->
        <div id="background"></div>
        <header id="page-header"><a id="hamburger" href="#sidebar-nav" target="_self"><span class="line"></span><span
                    class="line"></span><span class="line"></span></a></header>
        <nav id="sidebar-nav">
            <header id="sidebar-header"><a id="codepen-link" href="https://www.codepen.io/seanfree" target="_blank"></a>
                <div id="profile-info">
                    <h3 id="profile-name">Sean Free</h3>
                    <h4 id="blurb">Navigation bar</h4>
                </div><a id="btn-more"><span class="dot"></span><span class="dot"></span><span class="dot"></span></a>


                <!-- 导航列表 -->
                <ul id="sidebar-nav-list">
                    <li class="sidebar-nav-item" id="nav-item-home"
                        style="font-size: x-large; margin-bottom: 20px; width: 300px; height: 65px;border-radius: 10%/50% ;"
                        @click="goHome"><a href="#"><i class="material-icons">投票</i>广场</a>
                    </li>
                    <li class="sidebar-nav-item" id="nav-item-social"
                        style="font-size: x-large; margin-bottom: 20px;width: 300px;height: 65px;border-radius: 10%/50% ;"
                        @click="goMyVote">
                        <a href="#"><i class="material-icons">我的</i>投票</a>
                    </li>
                    <li class="sidebar-nav-item" id="nav-item-mail"
                        style="font-size: x-large; margin-bottom: 20px;width: 300px;height: 65px;border-radius: 10%/50% ;"
                        @click="myParticipate"><a href="#"><i class="material-icons">我的</i>参与</a>
                    </li>
                    <li class="sidebar-nav-item" id="nav-item-pictures"
                        style="font-size: x-large; margin-bottom: 20px;width: 300px;height: 65px;border-radius: 10%/50% ;"
                        @click="myAccount"><a href="#"><i class="material-icons">退出</i>登录</a></li>
                    <li class="sidebar-nav-item" id="nav-item-pictures"
                        style="font-size: x-large;width: 300px;height: 65px;border-radius: 10%/50% ;"><a href="#"><i
                                class="material-icons">关闭</i>导航</a></li>
                </ul>
            </header>
        </nav>

        <!-- 输入框，模糊查询 -->
        <div>
            <el-input v-model="input" placeholder="请输入要查询内容" style="position: absolute; 
      top: 68px; width: 300px; left: 950px;">
            </el-input>
            <el-button type="primary" icon="el-icon-search" style="position: absolute; 
      top: 68px; left: 1260px; width: 100px;" @click="search">搜索</el-button>
        </div>


        <!-- 投票数据生成表格 -->
        <el-table :data="tableData.slice((currentPage - 1) * pageSize, currentPage * pageSize)" class="table"
            style="font-size: 20px; text-align: center;" :row-class-name="tableRowClassName">
            <el-table-column prop="title" label="投票标题" width="260px" class="tableRow">
            </el-table-column>
            <el-table-column prop="userId" label="发起者" width="100px">
            </el-table-column>
            <el-table-column prop="createTime" label="发起时间" width="180px">
            </el-table-column>
            <el-table-column prop="account" label="参与人数" width="120px">
            </el-table-column>
            <el-table-column prop="choice" label="选择" width="360px">
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button type="danger" @click="deleteMyVote(scope.row)" icon="el-icon-delete" circle></el-button>
                    <!-- 修改投票信息 -->
                    <el-button type="primary" @click="modifyMyVote(scope.row); dialogVisibleModify = true"
                        icon="el-icon-edit" circle></el-button>
                    <el-button type="info" @click="stop(scope.row);" icon="el-icon-video-pause" circle></el-button>
                </template>

            </el-table-column>
        </el-table>

        <!-- 对表格分页 -->
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
            :page-size="pageSize" layout="total, prev, pager, next, jumper" :total="this.tableData.length"
            style="position: absolute; left: 510px; top: 690px;" background>
        </el-pagination>

        <!-- 修改表单弹出 -->
        <el-dialog title="修改投票" :visible.sync="dialogVisibleModify" width="30%" style="text-align: center;">
            <el-form ref="formModify" :model="formModify" label-width="80px">
                <el-form-item label="投票标题">
                    <el-input v-model="formModify.title" placeholder="请输入标题" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="选项一">
                    <el-input v-model="formModify.choice1" placeholder="输入选项一内容"></el-input>
                </el-form-item>
                <el-form-item label="选项二">
                    <el-input v-model="formModify.choice2" placeholder="输入选项二内容"></el-input>
                </el-form-item>
                <el-form-item label="选项三">
                    <el-input v-model="formModify.choice3" placeholder="输入选项三内容"></el-input>
                </el-form-item>
                <el-form-item label="选项四">
                    <el-input v-model="formModify.choice4" placeholder="输入选项四内容"></el-input>
                </el-form-item>
                <el-form-item label="是否多选" style="text-align: left;">
                    <el-switch v-model="formModify.switch"></el-switch>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisibleModify = false">取 消</el-button>
                <el-button type="primary" @click="dialogVisibleModify = false; submitModify()">确 定</el-button>
            </span>

        </el-dialog>


        <!-- 表单弹出 -->
        <el-dialog title="投票内容" :visible.sync="dialogVisible" width="30%">
            <div style="font-size: x-large; text-align: center;position: relative; top: -30px;">{{ choice.title }}</div>

            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="">
                    <el-radio-group v-model="form.number">
                        <el-radio label="1"
                            style="display: block; position: relative; top: -15px;left: -20px;transform:scale(1.15)"
                            id="choice1">{{
                                choice.choice1 }}</el-radio>
                        <el-radio label="2"
                            style="display: block; position: relative;top: 5px;left: -20px;transform:scale(1.15)"
                            id="choice2">{{
                                choice.choice2 }}</el-radio>
                        <el-radio label="3"
                            style="display: block; position: relative;top:25px; left: -20px;transform:scale(1.15)"
                            id="choice3">{{
                                choice.choice3 }}</el-radio>
                        <el-radio label="4"
                            style="display: block;position: relative; left:-18px; top: 45px;transform:scale(1.15)"
                            id="choice4">{{
                                choice.choice4 }}</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogVisible = false; submit()">确 定</el-button>
            </span>
        </el-dialog>


        <!-- 添加新的投票 -->
        <el-button type="success" icon="el-icon-plus" style=" height: 60px; width: 60px; font-size: 30px;
    position: absolute; top:580px; right: 30px;" @click="dialogVisible2 = true" circle></el-button>

        <!-- 添加投票弹出表单 -->
        <div>
            <el-dialog title="创建投票" :visible.sync="dialogVisible2" width="30%" style="text-align: center;">
                <el-form ref="form2" :model="form2" label-width="80px">
                    <el-form-item label="投票标题">
                        <el-input v-model="form2.title" placeholder="请输入标题"></el-input>
                    </el-form-item>
                    <el-form-item label="选项一">
                        <el-input v-model="form2.choice1" placeholder="输入选项一内容"></el-input>
                    </el-form-item>
                    <el-form-item label="选项二">
                        <el-input v-model="form2.choice2" placeholder="输入选项二内容"></el-input>
                    </el-form-item>
                    <el-form-item label="选项三">
                        <el-input v-model="form2.choice3" placeholder="输入选项三内容"></el-input>
                    </el-form-item>
                    <el-form-item label="选项四">
                        <el-input v-model="form2.choice4" placeholder="输入选项四内容"></el-input>
                    </el-form-item>
                    <el-form-item label="是否多选" style="text-align: left;">
                        <el-switch v-model="form2.switch"></el-switch>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible2 = false">取 消</el-button>
                    <el-button type="primary" @click="dialogVisible2 = false; addNewVote()">确 定</el-button>
                </span>

            </el-dialog>


            <!-- 修改密码表单弹出 -->
            <el-dialog title="投票内容" :visible.sync="dialogVisiblePassword" width="30%">
                <div style="font-size: x-large; text-align: center;position: relative; top: -30px;">修改密码</div>
                <el-input placeholder="请输入原有密码" v-model="passwordBefore" show-password></el-input>
                <el-input placeholder="请输入更改密码" v-model="passwordNew" style="margin-top: 30px;" show-password></el-input>
                <el-input placeholder="请确认更改密码" v-model="passwordCheck" style="margin-top: 30px;" show-password></el-input>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisiblePassword = false">取 消</el-button>
                    <el-button type="primary" @click="dialogVisiblePassword = false; submitPassword()">确 定</el-button>
                </span>
            </el-dialog>
        </div>
    </div>
</template>
    
<script>
import axios from 'axios';
var username = window.sessionStorage.getItem('username');

function Choice(title, choice1, choice2, choice3, choice4) {
    this.title = title;
    this.choice1 = choice1;
    this.choice2 = choice2;
    this.choice3 = choice3;
    this.choice4 = choice4;
}

function Vote(title, userId, choice) {
    this.title = title;
    this.userId = userId;
    this.choice = choice;
}


export default {
    methods: {

        //停止投票
        stop: function (val) {
            axios.get("http://localhost:8080/vote/stop", {
                params: {
                    title: val.title,
                    username: username,
                }
            })
                .then(res => {
                    if (res.data.code == 1) {
                        this.$message({
                            type: 'success',
                            message: '该投票已成功暂停！'
                        });
                    } else {
                        this.$message({
                            type: 'success',
                            message: '该投票已再次发起',
                        });
                    }
                })
        },

        // 修改密码
        submitPassword: function () {
            console.log(this.passwordBefore + '\n'
                + this.passwordNew + '\n'
                + this.passwordCheck + '\n');
        },

        // 流星雨
        playMusic() {
            this.audio.loop = this.isMusicLooping;
            this.audio.play();
            this.isMusicPlaying = true;
        },
        pauseMusic() {
            this.audio.pause();
            this.isMusicPlaying = false;
        },

        toggleMusic() {
            if (this.isMusicPlaying) {
                this.pauseMusic();
            } else {
                this.playMusic();
            }
        },
        createStars(context) {
            const starCount = 800;
            for (let i = 0; i < starCount; i++) {
                const star = {
                    x: Math.random() * window.innerWidth,
                    y: 5000 * Math.random(),
                    text: '.',
                    color: '',
                    getColor() {
                        const r = Math.random();
                        if (r < 0.5) {
                            this.color = '#333';
                        } else {
                            this.color = 'white';
                        }
                    },
                    draw() {
                        context.fillStyle = this.color;
                        context.fillText(this.text, this.x, this.y);
                    },
                    init() {
                        this.getColor();
                    },
                };
                star.init();
                star.draw();
            }
        },
        createMeteors(context) {
            const rainCount = 20;
            const rains = [];

            class MeteorRain {
                constructor() {
                    this.x = -1;
                    this.y = -1;
                    this.length = -1;
                    this.angle = 30;
                    this.width = -1;
                    this.height = -1;
                    this.speed = 1;
                    this.offset_x = -1;
                    this.offset_y = -1;
                    this.alpha = 1;
                    this.color1 = '';
                    this.color2 = '';

                    this.init();
                }

                init() {
                    this.getPos();
                    this.alpha = 1;
                    this.getRandomColor();
                    const x = Math.random() * 80 + 150;
                    this.length = Math.ceil(x);
                    this.angle = 30;
                    const speed = Math.random() + 0.5;
                    this.speed = Math.ceil(speed);
                    const cos = Math.cos(this.angle * Math.PI / 180);
                    const sin = Math.sin(this.angle * Math.PI / 180);
                    this.width = this.length * cos;
                    this.height = this.length * sin;
                    this.offset_x = this.speed * cos;
                    this.offset_y = this.speed * sin;
                }

                getRandomColor() {
                    const a = Math.ceil(255 - 240 * Math.random());
                    this.color1 = `rgba(${a},${a},${a},1)`;
                    this.color2 = 'black';
                }

                countPos() {
                    this.x = this.x - this.offset_x;
                    this.y = this.y + this.offset_y;
                }

                getPos() {
                    this.x = Math.random() * window.innerWidth;
                    this.y = Math.random() * window.innerHeight;
                }

                draw() {
                    context.save();
                    context.beginPath();
                    context.lineWidth = 1;
                    context.globalAlpha = this.alpha;
                    const line = context.createLinearGradient(this.x, this.y, this.x + this.width, this.y - this.height);
                    line.addColorStop(0, 'white');
                    line.addColorStop(0.3, this.color1);
                    line.addColorStop(0.6, this.color2);
                    context.strokeStyle = line;
                    context.moveTo(this.x, this.y);
                    context.lineTo(this.x + this.width, this.y - this.height);
                    context.closePath();
                    context.stroke();
                    context.restore();
                }

                move() {
                    const x = this.x + this.width - this.offset_x;
                    const y = this.y - this.height;
                    context.clearRect(x - 3, y - 3, this.offset_x + 5, this.offset_y + 5);
                    this.countPos();
                    this.alpha -= 0.002;
                    this.draw();
                }
            }

            for (let i = 0; i < rainCount; i++) {
                const rain = new MeteorRain();
                rains.push(rain);
            }

            const playRains = () => {
                for (let i = 0; i < rainCount; i++) {
                    const rain = rains[i];
                    rain.move();
                    if (rain.y > window.innerHeight) {
                        context.clearRect(rain.x, rain.y - rain.height, rain.width, rain.height);
                        rains[i] = new MeteorRain();
                    }
                }
                requestAnimationFrame(playRains);
            };

            playRains();
        },



        //表格内颜色
        tableRowClassName({ rowIndex }) {
            if (rowIndex % 4 == 0) {
                return 'warning-row';
            } else if (rowIndex % 4 === 2) {
                return 'success-row';
            }
            return '';
        },

        //分页相关
        handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
        },
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.currentPage = val;

        },

        //创建新的投票
        addNewVote: function () {
            axios.post("http://localhost:8080/vote", new Vote(this.form2.title, this.username,
                this.form2.choice1 + "，" + this.form2.choice2 + "，" +
                this.form2.choice3 + "，" + this.form2.choice4))
                .then(res => {
                    console.log(res)
                })
        },

        //删除我发起的投票
        /*
        title
        userId
        createTime
         */
        deleteMyVote: function (val) {
            this.title = val.title;
            this.userId = val.userId;

            this.$confirm('是否要删除该投票', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios.delete("http://localhost:8080/vote", {
                    params: {
                        title: this.title,
                        userId: this.userId,
                    }
                })
                    .then(res => {
                        console.log(res);
                        this.$message({
                            type: 'success',
                            message: '删除成功'
                        });
                        setTimeout(() => {
                            window.location.reload();
                        }, 800);
                    })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消操作'
                });
            });
        },

        //修改前置入表单中已有数据
        modifyMyVote: function (val) {
            console.log("modify");
            this.formModify.title = val.title;
            let strings = val.choice.split("，");
            console.log(strings);
            this.formModify.choice1 = strings[0];
            this.formModify.choice2 = strings[1];
            this.formModify.choice3 = strings[2];
            this.formModify.choice4 = strings[3];
        },

        //提交修改表单
        submitModify: function () {
            let s = this.formModify.choice1 + "，" + this.formModify.choice2 + "，"
                + this.formModify.choice3 + "，" + this.formModify.choice4;
            this.$confirm('是否要修改该投票，若进行修改，投票结果将被清除', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios.put("http://localhost:8080/vote", new Vote(this.formModify.title, this.username, s))
                    .then(res => {
                        console.log(res);
                        setTimeout(() => {
                            window.location.reload();
                        }, 500);
                    })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消操作'
                });
            });
        },

        //弹出表单提交
        submit: function () {
            axios.get("http://localhost:8080/vote/participate", {
                params: {
                    username: this.username,
                    number: this.form.number,
                    title: this.title,
                    creatorId: this.userId,
                }
            })
                .then(res => {
                    // console.log(res)
                    if (res.data.code == 1) {
                        this.$message({
                            type: 'success',
                            message: '投票成功!'
                        });
                    } else {
                        this.$message({
                            type: 'success',
                            message: '提交失败!'
                        });
                    }
                })
        },

        //导航栏内容跳转
        goHome: function () {
            this.$router.push({
                name: 'home',
            });
        },

        //跳转到我的投票
        goMyVote: function () {
            this.$router.push({
                name: 'vote',
            });
        },

        //跳转到我的参与
        myParticipate: function () {
            this.$router.push('/participate');
        },

        //跳转到我的账户
        myAccount: function () {
            this.$router.push('/log');
        },

        //模糊查询表格内容
        search: function () {
            // alert(this.input);
            axios.get("http://localhost:8080/vote/searchMyVote", {
                params: {
                    content: this.input,
                    creatorId: this.username,
                }
            })
                .then(res => {
                    this.tableData = res.data.data;
                    //location.reload();
                })
        }
    },

    //钩子函数
    mounted: function () {


        if (location.href.indexOf("#reloaded") == -1) {
            location.href = location.href + "#reloaded";
            location.reload();
        }

        axios.get("http://localhost:8080/vote/myVote", {
            params: {
                id: this.username,
            }
        })
            .then(res => {
                this.tableData = res.data.data;
                console.log(this.tableData);
            })

        this.audio = new Audio(require('@/assets/1.mp3'));
        // this.playMusic();
        const stars = document.getElementById('stars');
        const context = stars.getContext('2d');

        const windowWidth = window.innerWidth;
        const windowHeight = window.innerHeight;
        stars.width = windowWidth;
        stars.height = windowHeight;

        this.createStars(context);
        this.createMeteors(context);
    },
    data() {
        return {
            tableData: [],
            input: '',
            currentPage: 1,
            pageSize: 8,
            choice: new Choice(),
            choices: [],
            username,
            title: '',
            userId: '',
            // 音乐
            audio: null,
            isMusicPlaying: false,
            isMusicLooping: true,

            // 修改密码功能信息获取
            passwordBefore: '',
            passwordNew: '',
            passwordCheck: '',

            dialogVisible: false,
            form: {
                name: '',
                number: ''
            },
            dialogVisible2: false,
            form2: {
                title: '',
                choice1: '',
                choice2: '',
                choice3: '',
                choice4: '',
                switch: false,
            },
            dialogVisibleModify: false,
            formModify: {
                title: '',
                choice1: '',
                choice2: '',
                choice3: '',
                choice4: '',
                switch: false,
            },
            dialogVisiblePassword: false,
        }
    }
}
</script>
<style>
body {
    overflow: hidden;
}

.table {
    position: absolute;
    top: 115px;
    left: 160px;
    width: 1200px;
    height: 566px;
    opacity: 0.85;
}

.el-table .warning-row {
    background: oldlace;
}

.el-table .success-row {
    background: #f0f9eb;
}

::-moz-selection {
    background: transparent;
}

::selection {
    background: transparent;
}

a {
    color: white;
    text-decoration: none;
}

#background {
    position: fixed;
    z-index: -1;
    top: 0;
    left: 0;
    height: 100vh;
    width: 100vw;
    background-image: url("../assets/homeBcg.png");
    background-size: cover;
    background-position: center center;
    opacity: 0.95;
}

#fab {
    position: fixed;
    z-index: 1;
    right: 30px;
    bottom: 30px;
    height: 60px;
    width: 60px;
    border-radius: 50%;
    background-color: #f55e22;
    box-shadow: 0 2px 8px #3f3f3f;
    text-align: center;
    line-height: 60px;
    font-size: 1.75em;
    cursor: pointer;
}

#fab:active {
    transform: scale(0.95);
}

#page-header {
    position: fixed;
    z-index: 0;
    top: 0;
    left: 0;
    height: 60px;
    width: 100vw;
    background-color: #373838;
    /* background-image: url("../assets/homeBcg.png"); */
    box-shadow: 0 -3px 8px 5px #3f3f3f;
}

#page-header #hamburger {
    position: relative;
    display: block;
    top: 15px;
    left: 15px;
    height: 30px;
    width: 30px;
}

#page-header #hamburger .line {
    position: absolute;
    display: block;
    left: 5px;
    height: 2px;
    background-color: white;
}

#page-header #hamburger .line:first-of-type {
    transform: translateY(8px);
    width: 20px;
}

#page-header #hamburger .line:nth-of-type(2) {
    transform: translateY(15px);
    width: 18px;
}

#page-header #hamburger .line:last-of-type {
    transform: translateY(22px);
    width: 16px;
}

#page-header #hamburger:active .line:first-of-type {
    transform: translateY(10px);
}

#page-header #hamburger:active .line:last-of-type {
    transform: translateY(20px);
}

#sidebar-nav {
    position: absolute;
    z-index: 3;
    top: 0;
    height: 100vh;
    width: 400px;
    left: 0px;
    /*background-color: #e51616;*/
    background-image: url("https://img1.imgtp.com/2023/07/14/IS1PLTb7.gif");
    box-shadow: 0 0 0 0 #353434;
    transform: translateX(-400px);
    transition: transform 0.5s ease-in-out;
}

#sidebar-nav #sidebar-header {
    position: relative;
    height: 200px;
    width: 100%;
    background-color: #0d92a3;
    background-image: url("https://img1.imgtp.com/2023/07/14/ElsGlvKZ.gif");
    background-size: cover;
}

#sidebar-nav #sidebar-header #codepen-link {
    position: relative;
    display: block;
    top: 30px;
    left: 30px;
    height: 80px;
    width: 80px;
    z-index: 10;
    border-radius: 50%;
    box-sizing: border-box;
    background-image: url("https://s3-us-west-2.amazonaws.com/s.cdpn.io/544318/logo.jpg");
    background-position: center center;
    background-size: cover;
    opacity: 0.6;
    transition: all 0.25s;
}

#sidebar-nav #sidebar-header #codepen-link:hover {
    opacity: 1;
    box-shadow: 0 2px 6px #0f0f0f;
}

#sidebar-nav #sidebar-header #profile-info {
    position: relative;
    top: 15px;
    padding: 30px;
}

#sidebar-nav #sidebar-header #profile-info #profile-name {
    font-size: 1.5em;
    margin-bottom: 5px;
}

#sidebar-nav #sidebar-header #btn-more {
    position: absolute;
    top: 20px;
    right: 20px;
    padding: 5px;
    cursor: pointer;
}

#sidebar-nav #sidebar-header #btn-more .dot {
    display: block;
    height: 4px;
    width: 4px;
    border-radius: 50%;
    margin-bottom: 5px;
    background-color: white;
}

#sidebar-nav #sidebar-nav-list {
    position: relative;
    /* background-color: #0e0101; */
    background: rgba(29, 2, 2, 0.5);
}

#sidebar-nav #sidebar-nav-list .sidebar-nav-item {
    position: relative;
    z-index: 4;
    box-shadow: 0 2px 4px transparent;
    opacity: 0;
    transform: translateX(-30px);
    transition: all 0.25s ease-in-out;
    -webkit-animation-fill-mode: forwards;
    animation-fill-mode: forwards;
    background-color: #2b2828;
}

#sidebar-nav #sidebar-nav-list .sidebar-nav-item a {
    display: flex;
    align-items: center;
    height: 100%;
    width: 100%;
    box-sizing: border-box;
    padding: 20px 0 20px 30px;
    color: #bcdef4fd;
}

#sidebar-nav #sidebar-nav-list .sidebar-nav-item a .material-icons {
    padding: 0 8px 2px 0;
    color: #f55e22;
}

#sidebar-nav #sidebar-nav-list .sidebar-nav-item:hover {
    z-index: 5;
    background-color: rgb(129, 126, 126);
    box-shadow: 0 2px 4px #6f6f6f;
}

#sidebar-nav #sidebar-nav-list .sidebar-nav-item:nth-child(1) {
    -webkit-animation-delay: 0.05s;
    animation-delay: 0.05s;
}

#sidebar-nav #sidebar-nav-list .sidebar-nav-item:nth-child(2) {
    -webkit-animation-delay: 0.1s;
    animation-delay: 0.1s;
}

#sidebar-nav #sidebar-nav-list .sidebar-nav-item:nth-child(3) {
    -webkit-animation-delay: 0.15s;
    animation-delay: 0.15s;
}

#sidebar-nav #sidebar-nav-list .sidebar-nav-item:nth-child(4) {
    -webkit-animation-delay: 0.2s;
    animation-delay: 0.2s;
}

#sidebar-nav #sidebar-nav-list .sidebar-nav-item:nth-child(5) {
    -webkit-animation-delay: 0.25s;
    animation-delay: 0.25s;
}

#sidebar-nav #sidebar-nav-list .sidebar-nav-item:nth-child(6) {
    -webkit-animation-delay: 0.3s;
    animation-delay: 0.3s;
}

#sidebar-nav #sidebar-nav-list .sidebar-nav-item:nth-child(7) {
    -webkit-animation-delay: 0.35s;
    animation-delay: 0.35s;
}

#sidebar-nav #sidebar-nav-list .sidebar-nav-item:nth-child(8) {
    -webkit-animation-delay: 0.4s;
    animation-delay: 0.4s;
}

#sidebar-nav:target {
    transform: translateX(0px);
    box-shadow: -4px 0 12px 6px #3f3f3f;
}

#sidebar-nav:target .sidebar-nav-item {
    -webkit-animation: nav-item-fade-in 1s ease-in-out;
    animation: nav-item-fade-in 1s ease-in-out;
}

#sidebar-nav:target+#nav-screen-overlay {
    display: block;
    pointer-events: auto;
    opacity: 0.3;
}

#nav-screen-overlay {
    position: fixed;
    z-index: 2;
    height: 100vh;
    width: 100vw;
    background-color: #0f0f0f;
    opacity: 0;
    cursor: default;
    pointer-events: none;
    transition: opacity 0.5s ease-in-out;
}

@-webkit-keyframes nav-item-fade-in {
    to {
        opacity: 1;
        transform: translateX(0px);
    }
}

@keyframes nav-item-fade-in {
    to {
        opacity: 1;
        transform: translateX(0px);
    }
}

/* 音乐按钮 */
.music-button {
    position: fixed;
    z-index: 1;
    right: 30px;
    bottom: 30px;
    height: 60px;
    width: 60px;
    border-radius: 50%;
    background-color: #817f7e;
    box-shadow: 0 2px 8px #3f3f3f;
    text-align: center;
    line-height: 60px;
    font-size: 1.75em;
    cursor: pointer;
}

.music-button.playing {
    background-color: #7d7f7f;
}

.music-button:active {
    transform: scale(0.95);
}

.music-button i {
    font-size: 0.7em;
}
</style>