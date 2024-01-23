<template>
  <div id="app">
    <router-view />
    <audio this.audio.loop="loop" preload="preload" id="bgmusic" this.audio.controls="controls" ref="MusicPlay"
      style="display: none;">
      <source src="assets/1.mp3" type="audio/mpeg" />
    </audio>
  </div>
</template>
