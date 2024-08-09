/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var count = 0;
var zoomLevel = 1;

var pt;

/* Incrémentation / décrémentation du point */
function edit(e, b) {
    switch(b) {
        
        /* clique gauche : on incrémente */
        case 1:
            /* alert('increment ' + e.parentElement.getAttributeNS(null, 'id'));*/
            
            quantity = e.parentElement.getAttributeNS(null, 'data-quantity');
            id = e.parentElement.getAttributeNS(null, 'id');
            id = id.slice("point-".length);
            /* Si quantité plus grand que 1 on va juste augmenteé le compteur en dessous */
            if(quantity > 1) {
                quantity++;
                e.parentElement.getElementsByTagName("text")[0].innerHTML = quantity;
                
            /* Sinon on doit changé la couleur et ajouté le compteur */
            } else {
                quantity++;
                var list = e.parentElement.getElementsByTagName("line");
                for(var i = 0; i < list.length; i++) {
                    list[i].setAttributeNS(null, 'stroke', 'yellow');
                }
                var text = document.createElementNS("http://www.w3.org/2000/svg", "text");
                text.setAttributeNS(null, 'fill', 'white');
                text.setAttributeNS(null, "x", "-5");
                text.setAttributeNS(null, "y", "20");
                text.innerHTML = quantity;
                e.parentElement.appendChild(text);
            }
            e.parentElement.setAttributeNS(null, 'data-quantity', quantity);
            /* TODO Java Fonction to add quatity. id + quantity */
            controller.editPoint(id, quantity);
        break;
        
        /* clique droit : on décrémente */
        case 3:
            /* alert('decrement ' + e.parentElement.getAttributeNS(null, 'id')); */
            
            quantity = e.parentElement.getAttributeNS(null, 'data-quantity');
            id = e.parentElement.getAttributeNS(null, 'id');
            id = id.slice("point-".length);
            if(quantity > 2) {
                quantity--;
                e.parentElement.getElementsByTagName("text")[0].innerHTML = quantity;
                
            } else if (quantity == 2) {
                quantity--;
                var list = e.parentElement.getElementsByTagName("line");
                for(var i = 0; i < list.length; i++) {
                    list[i].setAttributeNS(null, 'stroke', 'red');
                }
                e.parentElement.getElementsByTagName("text")[0].remove();
            } else {
                quantity--;
                e.parentElement.remove();
            }
            e.parentElement.setAttributeNS(null, 'data-quantity', quantity);
            /* appel fonction java pour modif bdd id + quantity */
            controller.editPoint(id, quantity);
        break;
        default:
    }
}

/* Creation d'un nouveau point */
function create(x, y) {
    var cross = document.createElementNS("http://www.w3.org/2000/svg", "g");
    var l1 = document.createElementNS("http://www.w3.org/2000/svg", "line");
    var l2 = document.createElementNS("http://www.w3.org/2000/svg", "line");
    l1.setAttribute("x1", "-5");
    l1.setAttribute("x2", "5");
    l1.setAttribute("y1", "-5");
    l1.setAttribute("y2", "5");
    l1.setAttribute("stroke", "red");
    l1.setAttribute("stroke-width", "2");
                
    l2.setAttribute("x1", "-5");
    l2.setAttribute("x2", "5");
    l2.setAttribute("y1", "5");
    l2.setAttribute("y2", "-5");
    l2.setAttribute("stroke", "red");
    l2.setAttribute("stroke-width", "2");
    cross.appendChild(l1);
    cross.appendChild(l2);
    cross.setAttribute("transform", "translate(" + x + " " + y  + ")");
    cross.setAttributeNS(null, 'data-quantity', 1);
    cross.setAttributeNS(null, "class", "points");
    cross.setAttributeNS(null, "id", "point-" + count);
    cross.setAttribute('style', 'cursor: pointer');
    cross.addEventListener('mousedown', function(e) {
        e.preventDefault(); e.stopPropagation(); edit(e.target, e.which);
    });
    cross.addEventListener('click', function(e) {
        e.preventDefault(); e.stopPropagation();
    });

    count++;
    
    document.getElementsByClassName("measurements")[0].appendChild(cross);
    /* appel fonction java pour modif bdd x + y + count/id */
    controller.createPoint(count, x, y);
}

/* Ajout des evenements pour interaction */
function start() {
    var list = document.getElementsByClassName('points');
    
    for(var i = 0; i < list.length; i++) {
        list[i].setAttribute('style', 'cursor: pointer');
        list[i].addEventListener('mousedown', function(e) {
            e.preventDefault(); e.stopPropagation(); edit(e.target, e.which);
        });
        list[i].addEventListener('click', function(e) {
            e.preventDefault(); e.stopPropagation();
        });
        count++;
    }
    
    document.onclick = function(e) {
        pt.x = e.clientX;
        pt.y = e.clientY;
        var cursorpt = pt.matrixTransform(svg.getScreenCTM().inverse());
        /*create((e.pageX/1.5) / zoomLevel, (e.pageY/1.5) / zoomLevel);*/
        create(cursorpt.x, cursorpt.y);
    };
    
    document.getElementsByTagName('svg')[0].style = 'cursor: crosshair';
    
    pt = document.getElementsByTagName('svg')[0].createSVGPoint();
}

/* Fonction pour zoom l'image */
function zoom(e) {
    var svg = document.getElementById("svg");
    svg.style.width = (e + "%");
    zoomLevel = e/100;
}

/* Fonction pour cacher les croix */
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

/* Executé à la fin du chargement de l'image */
window.onload = function() {
    start();
};
