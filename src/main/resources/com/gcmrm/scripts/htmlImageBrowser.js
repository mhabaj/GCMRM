/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* Fonction pour zoom l'image */
function hide(b) {
    var layers = document.getElementsByClassName("measurements");
    for (var i = 0; i < layers.length; i++) {
        if(b) {
            layers[i].style.opacity = 0;
        } else {
            layers[i].style.opacity = 1;
        }
    }
}