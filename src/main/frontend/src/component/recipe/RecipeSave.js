import React, {useState} from "react";

const RecipeSaveForm = ({onsubmit})=>{
    const {title, setTitle} = useState('');
    const {content, setContent} = useState('');
    const {ingredients , setIngredients} = useState('');
    const {dosage , setDosage} = useState('');
    const {situation, setSituation} = useState('');
    const {foodStuff, setFoodStuff} = useState('');
    const {foodNationType, setFoodNationType} = useState('');
    const {explanations, setExplanations} = useState('');
    const {foodImgFile, setFoodImgFile} = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();

        // RecipeDto 구조에 맞게 데이터를 구성
        const recipeDto = {
            title,
            content
        }
    };
}