import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [recipes, setRecipes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const [selectedRecipe, setSelectedRecipe] = useState(null);
  const [isAdding, setIsAdding] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  
  // Shared state for both Create and Update
  const [formData, setFormData] = useState({ name: '', ingredients: '', description: '' });

  useEffect(() => {
    fetchRecipes();
  }, []);

  const fetchRecipes = async () => {
    try {
      const response = await fetch('/api/recipes', {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
      });
      if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
      const data = await response.json();
      setRecipes(data);
      setLoading(false);
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
  e.preventDefault();
  
  if (isEditing) {
    try {
      const response = await fetch(`/api/recipes/${selectedRecipe.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });
      
      if (!response.ok) throw new Error(`Status: ${response.status}`);
      const message = await response.text();
      
      alert(message);
      fetchRecipes();
      setIsEditing(false);
      setSelectedRecipe(null);
    } catch (err) {
      alert("Failed to update recipe: " + err.message);
    }
  } else {
    try {
      const response = await fetch('/api/recipes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });
      
      if (!response.ok) throw new Error(`Status: ${response.status}`);
      const message = await response.text();
      
      alert(message);
      fetchRecipes();
      setIsAdding(false);
      setFormData({ name: '', ingredients: '', description: '' });
    } catch (err) {
      alert("Failed to add recipe: " + err.message);
    }
  }
};

const handleDelete = async (id) => {
  if (!window.confirm("Are you sure you want to delete this recipe?")) return;
  
  try {
    const response = await fetch(`/api/recipes/${id}`, {
      method: 'DELETE'
    });
    
    if (!response.ok) throw new Error(`Status: ${response.status}`);
    const message = await response.text();
    
    alert(message);
    setRecipes(recipes.filter(r => r.id !== id));
    setSelectedRecipe(null);
  } catch (err) {
    alert("Failed to delete recipe: " + err.message);
  }
};

  const openAddForm = () => {
    setIsAdding(true);
    setIsEditing(false);
    setSelectedRecipe(null);
    setFormData({ name: '', ingredients: '', description: '' });
  };

  const openEditForm = () => {
    setIsEditing(true);
    setFormData({
      name: selectedRecipe.name,
      ingredients: selectedRecipe.ingredients,
      description: selectedRecipe.description
    });
  };

  const cancelForm = () => {
    setIsAdding(false);
    setIsEditing(false);
    if (isEditing) setFormData({ name: '', ingredients: '', description: '' });
  };

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1>My Recipe Book</h1>
        <p>Your personal collection of culinary favorites</p>
        
        {!isAdding && !isEditing && (
          <button className="add-button" onClick={openAddForm}>
            + Add New Recipe
          </button>
        )}
      </div>
      
      {loading && <div className="status-message">Loading recipes...</div>}
      {error && <div className="status-message error-message"><strong>Error:</strong> {error}</div>}

      {(isAdding || isEditing) ? (
        <div className="form-view recipe-detail-card">
          <h2>{isEditing ? 'Edit Recipe' : 'Create a New Recipe'}</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Recipe Name</label>
              <input 
                type="text" 
                required 
                value={formData.name} 
                onChange={(e) => setFormData({...formData, name: e.target.value})} 
              />
            </div>
            
            <div className="form-group">
              <label>Ingredients</label>
              <textarea 
                required 
                rows="4"
                value={formData.ingredients} 
                onChange={(e) => setFormData({...formData, ingredients: e.target.value})} 
              />
            </div>

            <div className="form-group">
              <label>Description / Instructions</label>
              <textarea 
                required 
                rows="5"
                value={formData.description} 
                onChange={(e) => setFormData({...formData, description: e.target.value})} 
              />
            </div>

            <div className="button-group">
              <button type="submit" className="submit-button">
                {isEditing ? 'Update Recipe' : 'Save Recipe'}
              </button>
              <button type="button" className="cancel-button" onClick={cancelForm}>Cancel</button>
            </div>
          </form>
        </div>
      ) : selectedRecipe ? (
        <div className="detail-view">
          <button className="back-button" onClick={() => setSelectedRecipe(null)}>
            &larr; Back to Menu
          </button>
          
          <div className="recipe-detail-card">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
              <h2 className="recipe-title" style={{ margin: 0 }}>{selectedRecipe.name}</h2>
              <div style={{ display: 'flex', gap: '10px' }}>
                <button className="edit-button" onClick={openEditForm}>Edit</button>
                <button className="delete-button" onClick={() => handleDelete(selectedRecipe.id)}>Delete</button>
              </div>
            </div>
            
            <div className="recipe-section" style={{ marginBottom: '20px' }}>
              <h3 style={{ color: '#2c3e50', marginBottom: '8px' }}>Ingredients</h3>
              <p className="recipe-description">{selectedRecipe.ingredients}</p>
            </div>

            <div className="recipe-section">
              <h3 style={{ color: '#2c3e50', marginBottom: '8px' }}>Instructions</h3>
              <p className="recipe-description">{selectedRecipe.description}</p>
            </div>
          </div>
        </div>
      ) : (
        <div className="recipe-grid">
          {!loading && !error && recipes.map((recipe, index) => (
            <div key={index} className="recipe-card clickable" onClick={() => setSelectedRecipe(recipe)}>
              <h3 className="recipe-title">{recipe.name}</h3>
              <span className="click-hint">View Recipe &rarr;</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default App;