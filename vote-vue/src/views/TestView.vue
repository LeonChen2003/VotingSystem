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
        position: absolute; left: 700px; top: 8px; color: #f3eeee;">投票结果</span>


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

        <!-- 柱状图标题 -->
        <div style="font-size: xx-large; position: absolute; top: 100px; left: 625px; background-color: #ffffffc5;">{{
            this.title }}</div>

        <!-- 柱状图放置 -->
        <div class="echart" id="mychart" :style="myChartStyle"
            style="position: absolute; top: 160px; left: 370px; background-color: #ffffffc5;">
        </div>
        <!-- <el-button type="primary" @click="cli" style="position: absolute; top: 650px; left: 370px;">主要按钮</el-button> -->
        <el-button type="primary" @click="goBack" icon="el-icon-back" style="position: absolute; top: 165px; left: 375px;
        width: 45px; height: 45px; font-size: x-large;" circle></el-button>
    </div>
</template>
    
<script>
import * as echarts from "echarts";
var title = window.sessionStorage.getItem('title');
var choice = window.sessionStorage.getItem('choice').split('，');
var count1 = window.sessionStorage.getItem('count1');
var count2 = window.sessionStorage.getItem('count2');
var count3 = window.sessionStorage.getItem('count3');
var count4 = window.sessionStorage.getItem('count4');
// var username = window.sessionStorage.getItem('username');


export default {
    methods: {

        //饼状图初始化
        initDate() {
            for (let i = 0; i < this.pieData.length; i++) {
                // this.xData[i] = i;
                // this.yData =this.xData[i]*this.xData[i];
                this.pieName[i] = this.pieData[i].name;
            }
        },
        initEcharts() {
            // 饼图
            const option = {
                legend: {
                    // 图例
                    data: this.pieName,
                    right: "10%",
                    top: "30%",
                    orient: "vertical"
                },
                title: {
                    // 设置饼图标题，位置设为顶部居中
                    text: "国内院士前五省份图示",
                    top: "0%",
                    left: "center"
                },
                series: [
                    {
                        type: "pie",
                        label: {
                            show: true,
                            formatter: "{b} : {c} ({d}%)" // b代表名称，c代表对应值，d代表百分比
                        },
                        radius: "30%", //饼图半径
                        data: this.pieData
                    }
                ]
            };
            console.log(this.seriesData);
            // const optionFree = {
            //     xAxis: {},
            //     yAxis: {},
            //     series: [
            //         {
            //             data: this.seriesData,
            //             type: "line",
            //             smooth: true
            //         }
            //     ]
            // };
            this.myChart = echarts.init(document.getElementById("mychart"));
            this.myChart.setOption(option);
            //随着屏幕大小调节图表
            window.addEventListener("resize", () => {
                this.myChart.resize();
            });
        },

        //回退函数
        goBack: function () {
            this.$router.push('/home');
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
    },

    //钩子函数
    mounted: function () {
        if (location.href.indexOf("#reloaded") == -1) {
            location.href = location.href + "#reloaded";
            location.reload();
            this.drawCharts();
        }


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

        //初始化饼状图
        this.initDate(); //数据初始化
        this.initEcharts();
    },

    //数据
    data() {
        return {
            xData: [choice[0], choice[1], choice[2], choice[3]], //横坐标
            yData: [count1, count2, count3, count4], //数据
            myChartStyle: { float: "left", width: "800px", height: "500px" },//图表样式
            title,
            choice,
        };
    },
}
</script>
<style>
body {
    overflow: hidden;
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